package no.fint.oneroster.controller;

import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.response.OneRosterCollectionResponse;
import no.fint.oneroster.response.OneRosterItemResponse;
import no.fint.oneroster.service.EnrollmentService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<?> getAllEnrollments(@RequestParam(value = "filter", required = false) String filter,
                                               @RequestParam(value = "fields", required = false) String fields,
                                               Pageable pageable) {

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(enrollments, Enrollment.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/{sourcedId}")
    public ResponseEntity<?> getEnrollment(@PathVariable String sourcedId,
                                           @RequestParam(value = "fields", required = false) String fields) {

        Enrollment enrollment = enrollmentService.getEnrollment(sourcedId);

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(enrollment)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }
}
