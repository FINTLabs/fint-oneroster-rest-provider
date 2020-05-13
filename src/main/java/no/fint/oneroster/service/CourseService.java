package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final OneRosterService oneRosterService;

    public CourseService(OneRosterService oneRosterService) {
        this.oneRosterService = oneRosterService;
    }

    public List<Course> getAllCourses() {
        return oneRosterService.getAllCourses();
    }

    public Course getCourse(String sourcedId) {
        return getAllCourses()
                .stream()
                .filter(course -> course.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
