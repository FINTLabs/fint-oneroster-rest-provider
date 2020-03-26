package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.CourseFactory;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.properties.OrganisationProperties;
import no.fint.oneroster.repository.FintRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {

    private final FintRepository fintRepository;
    private final OrganisationProperties organisationProperties;

    public CourseService(FintRepository fintRepository, OrganisationProperties organisationProperties) {
        this.fintRepository = fintRepository;
        this.organisationProperties = organisationProperties;
    }

    public List<Course> getAllCourses(String orgId) {
        OrganisationProperties.Organisation organisation = organisationProperties.getOrganisations().get(orgId);

        List<Course> courses = new ArrayList<>();

        fintRepository.getLevels(orgId)
                .values()
                .forEach(level -> courses.add(CourseFactory.level(level, organisation)));

        fintRepository.getSubjects(orgId)
                .values()
                .forEach(subject -> courses.add(CourseFactory.subject(subject, organisation)));

        return courses;
    }

    public Course getCourse(String orgId, String sourcedId) {
        return getAllCourses(orgId).stream()
                .filter(course -> course.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
