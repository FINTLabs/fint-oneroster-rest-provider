package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.User;
import no.fint.oneroster.model.vocab.RoleType;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClazzService {
    private final OneRosterService oneRosterService;

    public ClazzService(OneRosterService oneRosterService) {
        this.oneRosterService = oneRosterService;
    }

    public List<Clazz> getAllClazzes() {
        return oneRosterService.getClazzes();
    }

    public Clazz getClazz(String sourcedId) {
        return Optional.ofNullable(oneRosterService.getClazzById(sourcedId))
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getStudentsForClazz(String sourcedId) {
        Clazz clazz = getClazz(sourcedId);

        return oneRosterService.getEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getClazz().getSourcedId().equals(clazz.getSourcedId()))
                .map(Enrollment::getUser)
                .map(GUIDRef::getSourcedId)
                .map(oneRosterService::getUserById)
                .filter(Objects::nonNull)
                .filter(user -> user.getRole().equals(RoleType.STUDENT))
                .collect(Collectors.toList());
    }

    public List<User> getTeachersForClazz(String sourcedId) {
        Clazz clazz = getClazz(sourcedId);

        return oneRosterService.getEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getClazz().getSourcedId().equals(clazz.getSourcedId()))
                .map(Enrollment::getUser)
                .map(GUIDRef::getSourcedId)
                .map(oneRosterService::getUserById)
                .filter(Objects::nonNull)
                .filter(user -> user.getRole().equals(RoleType.TEACHER))
                .collect(Collectors.toList());
    }
}
