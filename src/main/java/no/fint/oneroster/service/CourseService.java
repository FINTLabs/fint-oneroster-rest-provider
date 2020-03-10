package no.fint.oneroster.service;

import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.CourseFactory;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.repository.FintRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final FintRepository fintRepository;

    public CourseService(FintRepository fintRepository) {
        this.fintRepository = fintRepository;
    }

    public List<Course> getAllCourses(String orgId) {
        List<Course> courses = new ArrayList<>();

        Optional<OrganisasjonselementResource> schoolOwner = fintRepository.getOrganisationalElements(orgId)
                .values()
                .stream()
                .filter(OrgService.isSchoolOwner())
                .findAny();

        fintRepository.getLevels(orgId)
                .values()
                .forEach(level -> schoolOwner.ifPresent(owner -> courses.add(CourseFactory.level(level, owner))));

        fintRepository.getSubjects(orgId)
                .values()
                .forEach(subject -> schoolOwner.ifPresent(owner -> courses.add(CourseFactory.subject(subject, owner))));

        return courses;
    }

    public Course getCourse(String orgId, String sourcedId) {
        return getAllCourses(orgId).stream()
                .filter(course -> course.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
