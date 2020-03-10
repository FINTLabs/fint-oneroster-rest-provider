package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.service.CourseService;
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
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final ObjectMapper objectMapper;

    public CourseController(CourseService courseService, ObjectMapper objectMapper) {
        this.courseService = courseService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public Map<String, List<Course>> getAllCourses(@RequestHeader String orgId, FilterProvider fields, Pageable pageable) {
        List<Course> courses = courseService.getAllCourses(orgId);

        pageable.getSort().get().findFirst().ifPresent(sort -> {
            BeanComparator<Course> comparator = new BeanComparator<>(sort.getProperty());
            courses.sort(comparator);
        });

        List<Course> filteredCourses = courses.stream()
                .skip(pageable.getPageNumber())
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("courses", filteredCourses);
    }

    @GetMapping("/{sourcedId}")
    public Map<String, Course> getCourse(@RequestHeader String orgId, @PathVariable String sourcedId, FilterProvider fields) {
        Course course = courseService.getCourse(orgId, sourcedId);

        objectMapper.setFilterProvider(fields);

        return Collections.singletonMap("course", course);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
        log.error("WebClientException - Status: {}, Body: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
        return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
    }
}
