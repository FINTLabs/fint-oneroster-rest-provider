package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.model.User;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClazzService {
    private final OneRosterService oneRosterService;
    private final UserService userService;
    private final EnrollmentService enrollmentService;

    public ClazzService(OneRosterService oneRosterService, UserService userService, EnrollmentService enrollmentService) {
        this.oneRosterService = oneRosterService;
        this.userService = userService;
        this.enrollmentService = enrollmentService;
    }

    public List<Clazz> getAllClazzes() {
        return oneRosterService.getAllClazzes();
    }

    public Clazz getClazz(String sourcedId) {
        return getAllClazzes()
                .stream()
                .filter(clazz -> clazz.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<Clazz> getClazzesForStudent(String sourcedId) {
        User student = userService.getStudent(sourcedId);

        return enrollmentService.getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getUser().getSourcedId().equals(student.getSourcedId()))
                .map(Enrollment::getClazz)
                .flatMap(guidRef -> getAllClazzes()
                        .stream()
                        .filter(clazz -> clazz.getSourcedId().equals(guidRef.getSourcedId())))
                .collect(Collectors.toList());
    }

    public List<Clazz> getClazzesForTeacher(String sourcedId) {
        User teacher = userService.getTeacher(sourcedId);

        return enrollmentService.getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getUser().getSourcedId().equals(teacher.getSourcedId()))
                .map(Enrollment::getClazz)
                .flatMap(guidRef -> getAllClazzes()
                        .stream()
                        .filter(clazz -> clazz.getSourcedId().equals(guidRef.getSourcedId())))
                .collect(Collectors.toList());
    }

    public List<User> getStudentsForClazz(String sourcedId) {
        Clazz clazz = getClazz(sourcedId);

        return enrollmentService.getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getClazz().getSourcedId().equals(clazz.getSourcedId()))
                .map(Enrollment::getUser)
                .flatMap(guidRef -> userService.getAllStudents()
                        .stream()
                        .filter(student -> student.getSourcedId().equals(guidRef.getSourcedId())))
                .collect(Collectors.toList());
    }

    public List<User> getTeachersForClazz(String sourcedId) {
        Clazz clazz = getClazz(sourcedId);

        return enrollmentService.getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getClazz().getSourcedId().equals(clazz.getSourcedId()))
                .map(Enrollment::getUser)
                .flatMap(guidRef -> userService.getAllTeachers()
                        .stream()
                        .filter(teacher -> teacher.getSourcedId().equals(guidRef.getSourcedId())))
                .collect(Collectors.toList());
    }
}
