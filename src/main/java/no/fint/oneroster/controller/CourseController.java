package no.fint.oneroster.controller;

import no.fint.oneroster.model.Course;
import no.fint.oneroster.response.OneRosterCollectionResponse;
import no.fint.oneroster.response.OneRosterItemResponse;
import no.fint.oneroster.service.CourseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        OneRosterCollectionResponse response = new OneRosterCollectionResponse.Builder<>(courses, Course.class)
                .filter(filter)
                .pagingAndSorting(pageable)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getBody());
    }

    @GetMapping("/{sourcedId}")
    public ResponseEntity<?> getCourse(@PathVariable String sourcedId,
                                       @RequestParam(value = "fields", required = false) String fields) {

        Course course = courseService.getCourse(sourcedId);

        OneRosterItemResponse response = new OneRosterItemResponse.Builder<>(course)
                .fieldSelection(fields)
                .build();

        return ResponseEntity.ok(response.getBody());
    }
}
