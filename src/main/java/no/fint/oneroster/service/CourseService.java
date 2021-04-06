package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.repository.OneRosterRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {
    private final OneRosterRepository oneRosterRepository;

    public CourseService(OneRosterRepository oneRosterRepository) {
        this.oneRosterRepository = oneRosterRepository;
    }

    public List<Course> getAllCourses() {
        return oneRosterRepository.getCourses();
    }

    public Course getCourse(String sourcedId) {
        return Optional.ofNullable(oneRosterRepository.getCourseById(sourcedId))
                .orElseThrow(NotFoundException::new);
    }
}
