package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.OrgFactory;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.properties.OrganisationProperties;
import no.fint.oneroster.repository.FintRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrgService {

    private final FintRepository fintRepository;
    private final OrganisationProperties organisationProperties;

    public OrgService(FintRepository fintRepository, OrganisationProperties organisationProperties) {
        this.fintRepository = fintRepository;
        this.organisationProperties = organisationProperties;
    }

    public List<Org> getAllOrgs() {
        OrganisationProperties.Organisation organisation = organisationProperties.getOrganisation();

        Org schoolOwner = OrgFactory.schoolOwner(organisation);

        List<Org> orgs = fintRepository.getSchools()
                .values()
                .stream()
                .map(OrgFactory::school)
                .peek(school -> {
                    if (schoolOwner.getChildren() == null) {
                        schoolOwner.setChildren(new ArrayList<>());
                    }
                    school.setParent(GUIDRef.of(GUIDType.ORG, schoolOwner.getSourcedId()));
                    schoolOwner.getChildren().add(GUIDRef.of(GUIDType.ORG, school.getSourcedId()));
                })
                .collect(Collectors.toList());

        orgs.add(schoolOwner);

        return orgs;
    }

    public Org getOrg(String sourcedId) {
        return getAllOrgs()
                .stream()
                .filter(org -> org.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<Org> getAllSchools() {
        return getAllOrgs()
                .stream()
                .filter(org -> org.getType().equals(OrgType.SCHOOL))
                .collect(Collectors.toList());
    }

    public Org getSchool(String sourcedId) {
        return getAllSchools()
                .stream()
                .filter(school -> school.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    /*
    public Optional<Org> getSchoolOwner(String orgId) {
        return fintRepository.getOrganisationalElements(orgId)
                .values()
                .stream()
                .filter(isSchoolOwner())
                .map(OrgFactory::schoolOwner)
                .findAny();
    }

    public static Predicate<OrganisasjonselementResource> isSchoolOwner() {
        return organisasjonselementResource -> {
            if (organisasjonselementResource.getOverordnet().isEmpty()) return false;

            return organisasjonselementResource.getSelfLinks()
                    .stream()
                    .map(LinkUtil::normalize)
                    .findAny()
                    .map(id -> organisasjonselementResource.getOverordnet()
                            .stream()
                            .map(LinkUtil::normalize)
                            .anyMatch(id::equals))
                    .orElse(false);
        };
    }
     */
}