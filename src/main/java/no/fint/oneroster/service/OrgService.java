package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.repository.OneRosterRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrgService {
    private final OneRosterRepository oneRosterRepository;

    public OrgService(OneRosterRepository oneRosterRepository) {
        this.oneRosterRepository = oneRosterRepository;
    }

    public List<Org> getAllOrgs() {
        return oneRosterRepository.getAllOrgs();
    }

    public Org getOrg(String sourcedId) {
        return oneRosterRepository.getAllOrgs()
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