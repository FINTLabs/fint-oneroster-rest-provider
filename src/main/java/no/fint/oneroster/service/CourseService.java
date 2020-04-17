package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.CourseFactory;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.properties.OneRosterProperties;
import no.fint.oneroster.repository.FintEducationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private final FintEducationService fintEducationService;
    private final OneRosterProperties oneRosterProperties;

    public CourseService(FintEducationService fintEducationService, OneRosterProperties oneRosterProperties) {
        this.fintEducationService = fintEducationService;
        this.oneRosterProperties = oneRosterProperties;
    }

    public List<Course> getAllCourses() {
        OneRosterProperties.Org org = oneRosterProperties.getOrg();

        List<Course> courses = new ArrayList<>();

        fintEducationService.getLevels()
                .values()
                .stream()
                .distinct()
                .forEach(level -> courses.add(CourseFactory.level(level, org)));

        fintEducationService.getSubjects()
                .values()
                .stream()
                .distinct()
                .forEach(subject -> courses.add(CourseFactory.subject(subject, org)));

        return courses;
    }

    public Course getCourse(String sourcedId) {
        return getAllCourses()
                .stream()
                .filter(course -> course.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
