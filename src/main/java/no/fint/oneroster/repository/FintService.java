package no.fint.oneroster.repository;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FintService {
    private final FintRepository fintRepository;

    private final Map<String, String> selfLinks = new HashMap<>();
    private final Map<String, FintLinks> resources = new HashMap<>();

    public FintService(FintRepository fintRepository) {
        this.fintRepository = fintRepository;
    }

    public List<SkoleResource> getSchools() {
        return getResourcesByType(SkoleResource.class);
    }

    public SkoleResource getSchoolById(String id) {
        return getResourceByTypeAndId(SkoleResource.class, id);
    }

    public List<ElevResource> getStudents() {
        return getResourcesByType(ElevResource.class);
    }

    public ElevResource getStudentById(String id) {
        return getResourceByTypeAndId(ElevResource.class, id);
    }

    public List<SkoleressursResource> getTeachers() {
        return getResourcesByType(SkoleressursResource.class);
    }

    public SkoleressursResource getTeacherById(String id) {
        return getResourceByTypeAndId(SkoleressursResource.class, id);
    }

    public List<ElevforholdResource> getStudentRelations() {
        return getResourcesByType(ElevforholdResource.class);
    }

    public ElevforholdResource getStudentRelationById(String id) {
        return getResourceByTypeAndId(ElevforholdResource.class, id);
    }

    public List<UndervisningsforholdResource> getTeachingRelations() {
        return getResourcesByType(UndervisningsforholdResource.class);
    }

    public UndervisningsforholdResource getTeachingRelationById(String id) {
        return getResourceByTypeAndId(UndervisningsforholdResource.class, id);
    }

    public List<BasisgruppeResource> getBasisGroups() {
        return getResourcesByType(BasisgruppeResource.class);
    }

    public BasisgruppeResource getBasisGroupById(String id) {
        return getResourceByTypeAndId(BasisgruppeResource.class, id);
    }

    public List<KontaktlarergruppeResource> getContactTeacherGroups() {
        return getResourcesByType(KontaktlarergruppeResource.class);
    }

    public KontaktlarergruppeResource getContactTeacherGroupById(String id) {
        return getResourceByTypeAndId(KontaktlarergruppeResource.class, id);
    }

    public List<UndervisningsgruppeResource> getTeachingGroups() {
        return getResourcesByType(UndervisningsgruppeResource.class);
    }

    public UndervisningsgruppeResource getTeachingGroupById(String id) {
        return getResourceByTypeAndId(UndervisningsgruppeResource.class, id);
    }

    public List<ArstrinnResource> getLevels() {
        return getResourcesByType(ArstrinnResource.class);
    }

    public ArstrinnResource getLevelById(String id) {
        return getResourceByTypeAndId(ArstrinnResource.class, id);
    }

    public List<FagResource> getSubjects() {
        return getResourcesByType(FagResource.class);
    }

    public FagResource getSubjectById(String id) {
        return getResourceByTypeAndId(FagResource.class, id);
    }

    public List<PersonResource> getPersons() {
        return getResourcesByType(PersonResource.class);
    }

    public PersonResource getPersonById(String id) {
        return getResourceByTypeAndId(PersonResource.class, id);

    }

    public List<PersonalressursResource> getPersonnel() {
        return getResourcesByType(PersonalressursResource.class);
    }

    public PersonalressursResource getPersonnelById(String id) {
        return getResourceByTypeAndId(PersonalressursResource.class, id);
    }

    public <T extends FintLinks> List<T> getResourcesByType(Class<T> clazz) {
        return resources.values()
                .stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    private <T extends FintLinks> T getResourceByTypeAndId(Class<T> clazz, String id) {
        try {
            String selfLinks = this.selfLinks.get(id);
            return clazz.cast(resources.get(selfLinks));
        } catch (ClassCastException ex) {
            log.warn(id, ex);
            return null;
        }
    }

    public void update() {
        selfLinks.clear();
        resources.clear();

        Flux.concat(fintRepository.getEducationResources(SkoleResources.class, FintEndpoint.SCHOOL.getKey()),
                fintRepository.getEducationResources(PersonResources.class, FintEndpoint.PERSON.getKey()),
                fintRepository.getEducationResources(ElevResources.class, FintEndpoint.STUDENT.getKey()),
                fintRepository.getEducationResources(SkoleressursResources.class, FintEndpoint.TEACHER.getKey()),
                fintRepository.getEducationResources(ElevforholdResources.class, FintEndpoint.STUDENT_RELATION.getKey()),
                fintRepository.getEducationResources(UndervisningsforholdResources.class, FintEndpoint.TEACHING_RELATION.getKey()),
                fintRepository.getEducationResources(BasisgruppeResources.class, FintEndpoint.BASIS_GROUP.getKey()),
                fintRepository.getEducationResources(UndervisningsgruppeResources.class, FintEndpoint.TEACHING_GROUP.getKey()),
                fintRepository.getEducationResources(KontaktlarergruppeResources.class, FintEndpoint.CONTACT_TEACHER_GROUP.getKey()),
                fintRepository.getEducationResources(ArstrinnResources.class, FintEndpoint.LEVEL.getKey()),
                fintRepository.getEducationResources(FagResources.class, FintEndpoint.SUBJECT.getKey()),
                fintRepository.getAdministrationResources(PersonalressursResources.class, FintEndpoint.PERSONNEL.getKey()),
                fintRepository.getAdministrationResources(PersonResources.class, FintEndpoint.PERSON.getKey()))
                .toStream()
                .forEach(resource -> {
                    List<String> links = getSelfLinks(resource);
                    if (links.isEmpty())
                        return;
                    links.forEach(link -> selfLinks.put(link, links.toString()));
                    resources.put(links.toString(), resource);
                });
    }

    private <T extends FintLinks> List<String> getSelfLinks(T resource) {
        return Optional.ofNullable(resource.getSelfLinks())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Link::getHref)
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
