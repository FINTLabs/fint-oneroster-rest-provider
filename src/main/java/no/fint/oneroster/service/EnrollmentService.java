package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EnrollmentService {
    private final OneRosterService oneRosterService;
    private final OrgService orgService;

    public EnrollmentService(OneRosterService oneRosterService, OrgService orgService) {
        this.oneRosterService = oneRosterService;
        this.orgService = orgService;
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

    public List<Enrollment> getEnrollmentsForSchool(String sourcedId) {
        Org school = orgService.getSchool(sourcedId);

        return getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSchool().getSourcedId().equals(school.getSourcedId()))
                .collect(Collectors.toList());
    }
}
