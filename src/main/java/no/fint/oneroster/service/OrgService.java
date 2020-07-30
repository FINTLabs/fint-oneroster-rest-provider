package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.*;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.model.vocab.RoleType;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrgService {
    private final OneRosterService oneRosterService;
    private final AcademicSessionService academicSessionService;

    public OrgService(OneRosterService oneRosterService, AcademicSessionService academicSessionService) {
        this.oneRosterService = oneRosterService;
        this.academicSessionService = academicSessionService;
    }

    public List<Org> getAllOrgs() {
        return oneRosterService.getAllOrgs();
    }

    public Org getOrg(String sourcedId) {
        return oneRosterService.getAllOrgs()
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

    public List<Clazz> getClazzesForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return oneRosterService.getAllClazzes()
                .stream()
                .filter(clazz -> clazz.getSchool().getSourcedId().equals(school.getSourcedId()))
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return oneRosterService.getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSchool().getSourcedId().equals(school.getSourcedId()))
                .collect(Collectors.toList());
    }

    public List<User> getStudentsForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return oneRosterService.getAllUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.STUDENT) &&
                        user.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(school.getSourcedId()::equals))
                .collect(Collectors.toList());
    }

    public List<User> getTeachersForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return oneRosterService.getAllUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.STUDENT) &&
                        user.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(school.getSourcedId()::equals))
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsForClazzInSchool(String schoolSourcedId, String clazzSourcedId) {
        Org school = getSchool(schoolSourcedId);
        Clazz clazz = oneRosterService.getAllClazzes()
                .stream()
                .filter(c -> c.getSourcedId().equals(clazzSourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);

        return oneRosterService.getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSchool().getSourcedId().equals(school.getSourcedId()) &&
                        enrollment.getClazz().getSourcedId().equals(clazz.getSourcedId()))
                .collect(Collectors.toList());
    }

    public List<AcademicSession> getTermsForSchool(String sourcedId) {
        getSchool(sourcedId);

        return academicSessionService.getAllTerms();
    }
}