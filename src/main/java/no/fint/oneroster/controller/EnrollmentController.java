package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.service.EnrollmentService;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final ObjectMapper objectMapper;

    public EnrollmentController(EnrollmentService enrollmentService, ObjectMapper objectMapper) {
        this.enrollmentService = enrollmentService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public Map<String, List<Enrollment>> getAllEnrollments(@RequestHeader String orgId, FilterProvider fields, Pageable pageable) {
        List<Enrollment> enrollments = enrollmentService.getAllEnrollments(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<Enrollment> comparator = new BeanComparator<>(sort.getProperty());
            enrollments.sort(comparator);
        });

        List<Enrollment> filteredEnrollments = enrollments.stream()
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("enrollments", filteredEnrollments);
    }

    @GetMapping("/{sourcedId}")
    public Map<String, Enrollment> getEnrollment(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        Enrollment  enrollment = enrollmentService.getEnrollment(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("enrollment", enrollment);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("WebClientException - Status: {}, Body: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}
