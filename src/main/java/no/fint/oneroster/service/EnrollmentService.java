package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.repository.OneRosterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EnrollmentService {
    private final OneRosterRepository oneRosterRepository;

    public EnrollmentService(OneRosterRepository oneRosterRepository) {
        this.oneRosterRepository = oneRosterRepository;
    }

    public List<Enrollment> getAllEnrollments() {
        return oneRosterRepository.getEnrollments();
    }

    public Enrollment getEnrollment(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getEnrollmentById(sourcedId))
                .orElseThrow(NotFoundException::new);
    }
}
