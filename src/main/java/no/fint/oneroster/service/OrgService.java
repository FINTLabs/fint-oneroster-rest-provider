package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.*;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrgService {
    private final OneRosterService oneRosterService;
    private final EnrollmentService enrollmentService;
    private final UserService userService;
    private final ClazzService clazzService;

    public OrgService(OneRosterService oneRosterService, EnrollmentService enrollmentService, UserService userService, ClazzService clazzService) {
        this.oneRosterService = oneRosterService;
        this.enrollmentService = enrollmentService;
        this.userService = userService;
        this.clazzService = clazzService;
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

        return clazzService.getAllClazzes()
                .stream()
                .filter(clazz -> clazz.getSchool().getSourcedId().equals(school.getSourcedId()))
                .collect(Collectors.toList());
    }

    public List<Enrollment> getEnrollmentsForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return enrollmentService.getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSchool().getSourcedId().equals(school.getSourcedId()))
                .collect(Collectors.toList());
    }

    public List<User> getStudentsForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return userService.getAllStudents()
                .stream()
                .filter(student -> student.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(school.getSourcedId()::equals))
                .collect(Collectors.toList());
    }

    public List<User> getTeachersForSchool(String sourcedId) {
        Org school = getSchool(sourcedId);

        return userService.getAllTeachers()
                .stream()
                .filter(teacher -> teacher.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(school.getSourcedId()::equals))
                .collect(Collectors.toList());
    }
}