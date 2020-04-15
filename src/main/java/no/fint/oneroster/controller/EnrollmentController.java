package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.service.EnrollmentService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

        List<Enrollment> modifiedEnrollments = new OneRosterResponse.Builder<>(enrollments)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("enrollments", modifiedEnrollments));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Enrollment.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(enrollments.size()))
                .body(body);
    }

    @GetMapping("/enrollments/{sourcedId}")
    public ResponseEntity<?> getEnrollment(@PathVariable String sourcedId,
                                           @RequestParam(value = "fields", required = false) String fields) {

        Enrollment enrollment = enrollmentService.getEnrollment(sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("enrollment", enrollment));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Enrollment.class, fields)));

        return ResponseEntity.ok(body);
    }

    @GetMapping("/schools/{sourcedId}/enrollments")
    public ResponseEntity<?> getEnrollmentsForSchool(@PathVariable String sourcedId, Pageable pageable,
                                                     @RequestParam(value = "filter", required = false) String filter,
                                                     @RequestParam(value = "fields", required = false) String fields) {

        List<Enrollment> enrollments = enrollmentService.getEnrollmentsForSchool(sourcedId);

        List<Enrollment> modifiedEnrollments = new OneRosterResponse.Builder<>(enrollments)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("enrollments", modifiedEnrollments));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Enrollment.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(enrollments.size()))
                .body(body);
    }
}
