package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.repository.OneRosterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final OneRosterService oneRosterService;

    public CourseService(OneRosterService oneRosterService) {
        this.oneRosterService = oneRosterService;
    }

    public List<Course> getAllCourses() {
        return oneRosterService.getCourses();
    }

    public Course getCourse(String sourcedId) {
        return Optional.ofNullable(oneRosterService.getCourseById(sourcedId))
                .orElseThrow(NotFoundException::new);
    }
}
