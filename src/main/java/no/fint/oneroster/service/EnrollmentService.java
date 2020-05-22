package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EnrollmentService {
    private final OneRosterService oneRosterService;

    public EnrollmentService(OneRosterService oneRosterService) {
        this.oneRosterService = oneRosterService;
    }

    public List<Enrollment> getAllEnrollments() {
        return oneRosterService.getAllEnrollments();
    }

    public Enrollment getEnrollment(String sourcedId) {
        return getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
