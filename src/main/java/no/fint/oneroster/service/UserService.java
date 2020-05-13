package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.User;
import no.fint.oneroster.model.vocab.RoleType;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final OneRosterService oneRosterService;

    public UserService(OneRosterService oneRosterService) {
        this.oneRosterService = oneRosterService;
    }

    public List<User> getAllUsers() {
        return oneRosterService.getAllUsers();
    }

    public User getUser(String sourcedId) {
        return getAllUsers()
                .stream()
                .filter(user -> user.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getAllStudents() {
        return getAllUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.STUDENT))
                .collect(Collectors.toList());
    }

    public User getStudent(String sourcedId) {
        return getAllStudents()
                .stream()
                .filter(student -> student.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getStudentsForSchool(String sourcedId) {
        return getAllStudents()
                .stream()
                .filter(student -> student.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(sourcedId::equals))
                .collect(Collectors.toList());
    }

    public List<User> getAllTeachers() {
        return getAllUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.TEACHER))
                .collect(Collectors.toList());
    }

    public User getTeacher(String sourcedId) {
        return getAllTeachers()
                .stream()
                .filter(teacher -> teacher.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getTeachersForSchool(String sourcedId) {
        return getAllTeachers()
                .stream()
                .filter(teacher -> teacher.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(sourcedId::equals))
                .collect(Collectors.toList());
    }
}
