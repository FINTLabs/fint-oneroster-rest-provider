package no.fint.oneroster.service;

import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.OrgFactory;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.repository.FintRepository;
import no.fint.oneroster.util.LinkUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class OrgService {

    private final FintRepository fintRepository;

    public OrgService(FintRepository fintRepository) {
        this.fintRepository = fintRepository;
    }

    public List<Org> getAllOrgs(String orgId) {
        Optional<Org> schoolOwner = getSchoolOwner(orgId);

        List<Org> orgs = fintRepository.getSchools(orgId)
                .values()
                .stream()
                .map(OrgFactory::school)
                .peek(school -> schoolOwner.ifPresent(owner -> {
                    if (owner.getChildren() == null) {
                        owner.setChildren(new ArrayList<>());
                    }
                    school.setParent(GUIDRef.of(GUIDType.ORG, owner.getSourcedId()));
                    owner.getChildren().add(GUIDRef.of(GUIDType.ORG, school.getSourcedId()));
                }))
                .collect(Collectors.toList());

        schoolOwner.ifPresent(orgs::add);

        return orgs;
    }

    public Org getOrg(String orgId, String sourcedId) {
        return getAllOrgs(orgId).stream()
                .filter(org -> org.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<Org> getAllSchools(String orgId) {
        return getAllOrgs(orgId)
                .stream()
                .filter(org -> org.getType().equals(OrgType.SCHOOL))
                .collect(Collectors.toList());
    }

    public Org getSchool(String orgId, String sourcedId) {
        return getAllSchools(orgId).stream()
                .filter(school -> school.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

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
}