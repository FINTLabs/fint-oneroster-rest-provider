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

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClazzService {
    private final OneRosterRepository oneRosterRepository;

    public ClazzService(OneRosterRepository oneRosterRepository) {
        this.oneRosterRepository = oneRosterRepository;
    }

    public List<Clazz> getAllClazzes() {
        return oneRosterRepository.getClazzes();
    }

    public Clazz getClazz(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getClazzById(sourcedId))
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getStudentsForClazz(String sourcedId) {
        Clazz clazz = getClazz(sourcedId);

        return oneRosterRepository.getEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getClazz().getSourcedId().equals(clazz.getSourcedId()))
                .map(Enrollment::getUser)
                .map(GUIDRef::getSourcedId)
                .map(oneRosterRepository::getUserById)
                .filter(Objects::nonNull)
                .filter(user -> user.getRole().equals(RoleType.STUDENT))
                .collect(Collectors.toList());
    }

    public List<User> getTeachersForClazz(String sourcedId) {
        Clazz clazz = getClazz(sourcedId);

        return oneRosterRepository.getEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getClazz().getSourcedId().equals(clazz.getSourcedId()))
                .map(Enrollment::getUser)
                .map(GUIDRef::getSourcedId)
                .map(oneRosterRepository::getUserById)
                .filter(Objects::nonNull)
                .filter(user -> user.getRole().equals(RoleType.TEACHER))
                .collect(Collectors.toList());
    }
}
