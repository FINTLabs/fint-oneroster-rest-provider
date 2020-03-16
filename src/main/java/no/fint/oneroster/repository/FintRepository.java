package no.fint.oneroster.repository;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResources;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.PersonResources;
import no.fint.model.resource.utdanning.elev.*;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.FagResources;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResources;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResources;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResources;
import no.fint.oneroster.properties.OrganisationProperties;
import no.fint.oneroster.util.LinkUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class FintRepository {
    private final WebClient webClient;
    private final Authentication principal;
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final OrganisationProperties organisationProperties;

    public FintRepository(WebClient webClient, Authentication principal, OAuth2AuthorizedClientManager authorizedClientManager, OrganisationProperties organisationProperties) {
        this.webClient = webClient;
        this.principal = principal;
        this.authorizedClientManager = authorizedClientManager;
        this.organisationProperties = organisationProperties;
    }

    /*
    TODO - scheduling and updating
     */

    @Cacheable(value = "organisationalElements")
    public Map<String, OrganisasjonselementResource> getOrganisationalElements(String orgId) {
        Map<String, OrganisasjonselementResource> resources = new HashMap<>();

        getResources(orgId, OrganisasjonselementResources.class, "/administrasjon/organisasjon/organisasjonselement")
                .flatMapIterable(OrganisasjonselementResources::getContent)
                .filter(organisasjonselementResource -> Optional.ofNullable(organisasjonselementResource.getOrganisasjonsId()).map(Identifikator::getIdentifikatorverdi).isPresent() &&
                        Optional.ofNullable(organisasjonselementResource.getNavn()).isPresent())
                .collectList()
                .blockOptional()
                .ifPresent(organisations -> organisations
                        .forEach(organisation -> organisation.getSelfLinks()
                                .forEach(link -> resources.put(LinkUtil.normalize(link), organisation))
                        )
                );

        return resources;
    }

    @Cacheable(value = "schools")
    public Map<String, SkoleResource> getSchools(String orgId) {
        Map<String, SkoleResource> resources = new HashMap<>();

        getResources(orgId, SkoleResources.class, "/utdanning/utdanningsprogram/skole")
                .flatMapIterable(SkoleResources::getContent)
                .filter(skoleResource -> Optional.ofNullable(skoleResource.getSystemId()).map(Identifikator::getIdentifikatorverdi).isPresent() &&
                        Optional.ofNullable(skoleResource.getNavn()).isPresent())
                .collectList()
                .blockOptional()
                .ifPresent(schools -> schools
                        .forEach(school -> school.getSelfLinks()
                                .forEach(link -> resources.put(LinkUtil.normalize(link), school))
                        )
                );

        return resources;
    }

    @Cacheable(value = "persons")
    public Map<String, PersonResource> getPersons(String orgId) {
        Mono<PersonResources> teachersMono = getResources(orgId, PersonResources.class, "/administrasjon/personal/person");
        Mono<PersonResources> studentsMono = getResources(orgId, PersonResources.class, "/utdanning/elev/person");

        return Mono.zip(teachersMono, studentsMono, (teachers, students) -> {
            teachers.getContent().forEach(students::addResource);
            return students.getContent().stream()
                    .filter(personResource -> Optional.ofNullable(personResource.getNavn()).map(Personnavn::getFornavn).isPresent() &&
                            Optional.ofNullable(personResource.getNavn()).map(Personnavn::getEtternavn).isPresent())
                    .collect(Collectors.toMap(this::getSelfLink, Function.identity(), (a, b) -> a));
        }).block();
    }

    @Cacheable(value = "students")
    public Map<String, ElevResource> getStudents(String orgId) {
        Map<String, ElevResource> resources = new HashMap<>();

        getResources(orgId, ElevResources.class, "/utdanning/elev/elev")
                .flatMapIterable(ElevResources::getContent)
                .filter(elevResource -> Optional.ofNullable(elevResource.getSystemId()).map(Identifikator::getIdentifikatorverdi).isPresent() &&
                        Optional.ofNullable(elevResource.getBrukernavn()).map(Identifikator::getIdentifikatorverdi).isPresent())
                .collectList()
                .blockOptional()
                .ifPresent(students -> students
                        .forEach(student -> student.getSelfLinks()
                                .forEach(link -> resources.put(LinkUtil.normalize(link), student))
                        )
                );

        return resources;
    }

    @Cacheable(value = "teachers")
    public Map<String, SkoleressursResource> getTeachers(String orgId) {
        Map<String, SkoleressursResource> resources = new HashMap<>();

        getResources(orgId, SkoleressursResources.class, "/utdanning/elev/skoleressurs")
                .flatMapIterable(SkoleressursResources::getContent)
                .filter(teacher -> Optional.ofNullable(teacher.getSystemId()).map(Identifikator::getIdentifikatorverdi).isPresent())
                .collectList()
                .blockOptional()
                .ifPresent(teachers -> teachers
                        .forEach(teacher -> teacher.getSelfLinks()
                                .forEach(link -> resources.put(LinkUtil.normalize(link), teacher))
                        )
                );

        return resources;
    }

    @Cacheable(value = "personnelResources")
    public Map<String, PersonalressursResource> getPersonnelResources(String orgId) {
        Map<String, PersonalressursResource> resources = new HashMap<>();

        getResources(orgId, PersonalressursResources.class, "/administrasjon/personal/personalressurs")
                .flatMapIterable(PersonalressursResources::getContent)
                .filter(personalressursResource -> Optional.ofNullable(personalressursResource.getBrukernavn()).map(Identifikator::getIdentifikatorverdi).isPresent())
                .collectList()
                .blockOptional()
                .ifPresent(personnelResources -> personnelResources
                        .forEach(personnelResource -> personnelResource.getSelfLinks()
                                .forEach(link -> resources.put(LinkUtil.normalize(link), personnelResource))
                        )
                );

        return resources;
    }

    @Cacheable(value = "studentRelations")
    public Map<String, ElevforholdResource> getStudentRelations(String orgId) {
        return getResources(orgId, ElevforholdResources.class, "/utdanning/elev/elevforhold")
                .flatMapIterable(ElevforholdResources::getContent)
                .filter(studentRelation -> Optional.of(studentRelation.getSystemId()).map(Identifikator::getIdentifikatorverdi).isPresent())
                .collectMap(this::getSelfLink)
                .block();
    }

    @Cacheable(value = "teachingRelations")
    public Map<String, UndervisningsforholdResource> getTeachingRelations(String orgId) {
        return getResources(orgId, UndervisningsforholdResources.class, "/utdanning/elev/undervisningsforhold")
                .flatMapIterable(UndervisningsforholdResources::getContent)
                .filter(teachingRelation -> Optional.ofNullable(teachingRelation.getSystemId()).map(Identifikator::getIdentifikatorverdi).isPresent())
                .collectMap(this::getSelfLink)
                .block();
    }

    @Cacheable(value = "basisGroups")
    public Map<String, BasisgruppeResource> getBasisGroups(String orgId) {
        return getResources(orgId, BasisgruppeResources.class, "/utdanning/elev/basisgruppe")
                .flatMapIterable(BasisgruppeResources::getContent)
                .filter(basisGroup -> Optional.ofNullable(basisGroup.getSystemId()).map(Identifikator::getIdentifikatorverdi).isPresent() &&
                        Optional.ofNullable(basisGroup.getNavn()).isPresent())
                .collectMap(this::getSelfLink)
                .block();
    }

    @Cacheable(value = "teachingGroups")
    public Map<String, UndervisningsgruppeResource> getTeachingGroups(String orgId) {
        return getResources(orgId, UndervisningsgruppeResources.class, "/utdanning/timeplan/undervisningsgruppe")
                .flatMapIterable(UndervisningsgruppeResources::getContent)
                .filter(teachingGroup -> Optional.ofNullable(teachingGroup.getSystemId()).map(Identifikator::getIdentifikatorverdi).isPresent() &&
                        Optional.ofNullable(teachingGroup.getNavn()).isPresent())
                .collectMap(this::getSelfLink)
                .block();
    }

    @Cacheable(value = "levels")
    public Map<String, ArstrinnResource> getLevels(String orgId) {
        return getResources(orgId, ArstrinnResources.class, "/utdanning/utdanningsprogram/arstrinn")
                .flatMapIterable(ArstrinnResources::getContent)
                .filter(level -> Optional.ofNullable(level.getSystemId()).map(Identifikator::getIdentifikatorverdi).isPresent() &&
                        Optional.ofNullable(level.getNavn()).isPresent())
                .collectMap(this::getSelfLink)
                .block();
    }

    @Cacheable(value = "subjects")
    public Map<String, FagResource> getSubjects(String orgId) {
        return getResources(orgId, FagResources.class, "/utdanning/timeplan/fag")
                .flatMapIterable(FagResources::getContent)
                .filter(subject -> Optional.ofNullable(subject.getSystemId()).map(Identifikator::getIdentifikatorverdi).isPresent() &&
                        Optional.ofNullable(subject.getNavn()).isPresent())
                .collectMap(this::getSelfLink)
                .block();
    }

    public <T> Mono<T> getResources(String orgId, Class<T> clazz, String path) {
        log.info("({}) Updating {}...", orgId, path);

        OrganisationProperties.Organisation organisation = organisationProperties.getOrganisations().get(orgId);

        String uri = organisation.getEnvironment().concat(path);

        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(organisation.getRegistration())
                .principal(principal)
                .attributes(attrs -> {
                    attrs.put(OAuth2ParameterNames.USERNAME, organisation.getUsername());
                    attrs.put(OAuth2ParameterNames.PASSWORD, organisation.getPassword());
                }).build();

        //OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        return webClient.get()
                .uri(uri)
                //.attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(clazz);
    }

    private <T extends FintLinks> String getSelfLink(T resource) {
        return resource.getSelfLinks().stream()
                .map(LinkUtil::normalize)
                .findFirst()
                .orElse(null);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("WebClientException - Status: {}, Body: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}