package no.fint.oneroster.controller;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.service.CourseService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses(@RequestHeader(defaultValue = "pwf") String orgId, Pageable pageable,
                                           @RequestParam(value = "filter", required = false) String filter,
                                           @RequestParam(value = "fields", required = false) String fields) {
        List<Course> courses = courseService.getAllCourses(orgId);

        List<Course> modifiedCourses = new OneRosterResponse.Builder<>(courses)
                .filter(filter)
                .sort(pageable.getSort())
                .page(pageable)
                .build();

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("courses", modifiedCourses));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Course.class, fields)));

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(courses.size()))
                .body(body);
    }

    @GetMapping("/{sourcedId}")
    public ResponseEntity<?> getCourse(@RequestHeader(defaultValue = "pwf") String orgId, @PathVariable String sourcedId,
                                         @RequestParam(value = "fields", required = false) String fields) {
        Course course = courseService.getCourse(orgId, sourcedId);

        MappingJacksonValue body = new MappingJacksonValue(Collections.singletonMap("course", course));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", OneRosterResponse.getFieldSelection(Course.class, fields)));

        return ResponseEntity.ok(body);
    }
}
