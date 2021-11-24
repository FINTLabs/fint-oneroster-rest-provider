package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.User;
import no.fint.oneroster.model.vocab.RoleType;
import no.fint.oneroster.repository.OneRosterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final OneRosterRepository oneRosterRepository;

    public UserService(OneRosterRepository oneRosterRepository) {
        this.oneRosterRepository = oneRosterRepository;
    }

    public List<User> getAllUsers() {
        return oneRosterRepository.getUsers();
    }

    public User getUser(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getUserById(sourcedId))
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getAllStudents() {
        return getAllUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.STUDENT))
                .collect(Collectors.toList());
    }

    public User getStudent(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getUserById(sourcedId))
                .filter(user -> user.getRole().equals(RoleType.STUDENT))
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getAllTeachers() {
        return getAllUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.TEACHER))
                .collect(Collectors.toList());
    }

    public User getTeacher(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getUserById(sourcedId))
                .filter(user -> user.getRole().equals(RoleType.TEACHER))
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getAllAdministrators() {
        return getAllUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.ADMINISTRATOR))
                .collect(Collectors.toList());
    }

    public User getAdministrator(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getUserById(sourcedId))
                .filter(user -> user.getRole().equals(RoleType.ADMINISTRATOR))
                .orElseThrow(NotFoundException::new);
    }

    public List<Clazz> getClazzesForStudent(String sourcedId) {
        User student = getStudent(sourcedId);

        return oneRosterRepository.getEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getUser().getSourcedId().equals(student.getSourcedId()))
                .map(Enrollment::getClazz)
                .map(GUIDRef::getSourcedId)
                .map(oneRosterRepository::getClazzById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Clazz> getClazzesForTeacher(String sourcedId) {
        User teacher = getTeacher(sourcedId);

        return oneRosterRepository.getEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getUser().getSourcedId().equals(teacher.getSourcedId()))
                .map(Enrollment::getClazz)
                .map(GUIDRef::getSourcedId)
                .map(oneRosterRepository::getClazzById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}