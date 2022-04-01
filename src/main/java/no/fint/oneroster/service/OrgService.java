package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.*;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.model.vocab.RoleType;
import no.fint.oneroster.repository.OneRosterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrgService {
    private final OneRosterRepository oneRosterRepository;

    public OrgService(OneRosterRepository oneRosterRepository) {
        this.oneRosterRepository = oneRosterRepository;
    }

    public List<Org> getAllOrgs() {
        return oneRosterRepository.getOrgs();
    }

    public Org getOrg(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getOrgById(sourcedId))
                .orElseThrow(NotFoundException::new);
    }

    public List<Org> getAllSchools() {
        return getAllOrgs()
                .stream()
                .filter(org -> org.getType().equals(OrgType.SCHOOL))
                .collect(Collectors.toList());
    }

    public Org getSchool(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getOrgById(sourcedId))
                .filter(org -> org.getType().equals(OrgType.SCHOOL))
                .orElseThrow(NotFoundException::new);
    }

    public List<Clazz> getClazzesForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return oneRosterRepository.getClazzes()
                .stream()
                .filter(clazz -> clazz.getSchool().getSourcedId().equals(school.getSourcedId()))
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return oneRosterRepository.getEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSchool().getSourcedId().equals(school.getSourcedId()))
                .collect(Collectors.toList());
    }

    public List<User> getStudentsForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return oneRosterRepository.getUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.STUDENT) &&
                        user.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(school.getSourcedId()::equals))
                .collect(Collectors.toList());
    }

    public List<User> getTeachersForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return oneRosterRepository.getUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.TEACHER) &&
                        user.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(school.getSourcedId()::equals))
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsForClazzInSchool(String schoolSourcedId, String clazzSourcedId) {
        Org school = getSchool(schoolSourcedId);

        Clazz clazz = Optional.ofNullable(oneRosterRepository.getClazzById(clazzSourcedId))
                .orElseThrow(NotFoundException::new);

        return oneRosterRepository.getEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSchool().getSourcedId().equals(school.getSourcedId()) &&
                        enrollment.getClazz().getSourcedId().equals(clazz.getSourcedId()))
                .collect(Collectors.toList());
    }

    public List<AcademicSession> getTermsForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return oneRosterRepository.getClazzes()
                .stream()
                .filter(clazz -> clazz.getSchool().getSourcedId().equals(school.getSourcedId()))
                .flatMap(clazz -> clazz.getTerms().stream())
                .map(GUIDRef::getSourcedId)
                .distinct()
                .map(oneRosterRepository::getAcademicSessionById)
                .collect(Collectors.toList());
    }
}