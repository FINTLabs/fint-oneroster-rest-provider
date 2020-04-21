package no.fint.oneroster.controller;

import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.service.CourseService;
import no.fint.oneroster.util.OneRosterResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getAllCourses(@RequestParam(value = "filter", required = false) String filter,
                                           @RequestParam(value = "fields", required = false) String fields,
                                           Pageable pageable) {

        List<Course> courses = courseService.getAllCourses();

        OneRosterResponse<Course> oneRosterResponse = new OneRosterResponse<>(Course.class, "courses")
                .collection(courses)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields);

        return ResponseEntity.ok()
                .headers(oneRosterResponse.getHeaders())
                .body(oneRosterResponse.getBody());
    }

    @GetMapping("/{sourcedId}")
    public ResponseEntity<?> getCourse(@PathVariable String sourcedId,
                                       @RequestParam(value = "fields", required = false) String fields) {

        Course course = courseService.getCourse(sourcedId);

        OneRosterResponse<Course> oneRosterResponse = new OneRosterResponse<>(Course.class, "course")
                .item(course)
                .fieldSelection(fields);

        return ResponseEntity.ok(oneRosterResponse.getBody());
    }
}
