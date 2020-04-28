package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.repository.OneRosterRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EnrollmentService {
    private final OneRosterRepository oneRosterRepository;

    public EnrollmentService(OneRosterRepository oneRosterRepository) {
        this.oneRosterRepository = oneRosterRepository;
    }

    public List<Enrollment> getAllEnrollments() {
        return oneRosterRepository.getAllEnrollments();
    }

    public Enrollment getEnrollment(String sourcedId) {
        return getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<Enrollment> getEnrollmentsForSchool(String sourcedId) {
        return getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSchool().getSourcedId().equals(sourcedId))
                .collect(Collectors.toList());
    }
}
