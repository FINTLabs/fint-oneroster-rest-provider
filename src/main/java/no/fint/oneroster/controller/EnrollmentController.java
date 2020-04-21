package no.fint.oneroster.controller;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.service.EnrollmentService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/enrollments")
    public ResponseEntity<?> getAllEnrollments(@RequestParam(value = "filter", required = false) String filter,
                                               @RequestParam(value = "fields", required = false) String fields,
                                               Pageable pageable) {

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        OneRosterResponse<Enrollment> oneRosterResponse = new OneRosterResponse<>(Enrollment.class, "enrollments")
                .collection(enrollments)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/enrollments/{sourcedId}")
    public ResponseEntity<?> getEnrollment(@PathVariable String sourcedId,
                                           @RequestParam(value = "fields", required = false) String fields) {

        Enrollment enrollment = enrollmentService.getEnrollment(sourcedId);

        OneRosterResponse<Enrollment> oneRosterResponse = new OneRosterResponse<>(Enrollment.class, "enrollment")
                .item(enrollment)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }

    @GetMapping("/schools/{sourcedId}/enrollments")
    public ResponseEntity<?> getEnrollmentsForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                     @RequestParam(value = "filter", required = false) String filter,
                                                     @RequestParam(value = "fields", required = false) String fields) {

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsForSchool(sourcedId);

        OneRosterResponse<Enrollment> oneRosterResponse = new OneRosterResponse<>(Enrollment.class, "enrollments")
                .collection(enrollments)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }
}
