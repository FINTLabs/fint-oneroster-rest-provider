package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.CourseFactory;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.properties.OrganisationProperties;
import no.fint.oneroster.repository.FintEducationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private final FintEducationService fintEducationService;
    private final OrganisationProperties organisationProperties;

    public CourseService(FintEducationService fintEducationService, OrganisationProperties organisationProperties) {
        this.fintEducationService = fintEducationService;
        this.organisationProperties = organisationProperties;
    }

    public List<Course> getAllCourses() {
        OrganisationProperties.Organisation organisation = organisationProperties.getOrganisation();

        List<Course> courses = new ArrayList<>();

        fintEducationService.getLevels()
                .values()
                .stream()
                .distinct()
                .forEach(level -> courses.add(CourseFactory.level(level, organisation)));

        fintEducationService.getSubjects()
                .values()
                .stream()
                .distinct()
                .forEach(subject -> courses.add(CourseFactory.subject(subject, organisation)));

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
