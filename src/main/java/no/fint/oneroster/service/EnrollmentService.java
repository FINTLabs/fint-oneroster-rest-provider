package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.EnrollmentFactory;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.repository.FintEducationService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EnrollmentService {
    private final FintEducationService fintEducationService;

    public EnrollmentService(FintEducationService fintEducationService) {
        this.fintEducationService = fintEducationService;
    }

    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();

        fintEducationService.getStudentRelations()
                .values()
                .stream()
                .distinct()
                .forEach(elevforholdResource -> {
                    Optional<ElevResource> student = elevforholdResource.getElev()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getStudents()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    Optional<SkoleResource> school = elevforholdResource.getSkole()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getSchools()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    elevforholdResource.getBasisgruppe()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getBasisGroups()::get)
                            .filter(Objects::nonNull)
                            .forEach(basisGroup -> {
                                if (student.isPresent() && school.isPresent()) {
                                    enrollments.add(EnrollmentFactory.student(elevforholdResource, student.get(), basisGroup, school.get()));
                                }
                            });

                    elevforholdResource.getUndervisningsgruppe()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getTeachingGroups()::get)
                            .filter(Objects::nonNull)
                            .forEach(teachingGroup -> {
                                if (student.isPresent() && school.isPresent()) {
                                    enrollments.add(EnrollmentFactory.student(elevforholdResource, student.get(), teachingGroup, school.get()));
                                }
                            });
                });

        fintEducationService.getTeachingRelations()
                .values()
                .stream()
                .distinct()
                .forEach(undervisningsforholdResource -> {
                    Optional<SkoleressursResource> teacher = undervisningsforholdResource.getSkoleressurs()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getTeachers()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    Optional<SkoleResource> school = undervisningsforholdResource.getSkole()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getSchools()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    undervisningsforholdResource.getBasisgruppe()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getBasisGroups()::get)
                            .filter(Objects::nonNull)
                            .forEach(basisGroup -> {
                                if (teacher.isPresent() && school.isPresent()) {
                                    enrollments.add(EnrollmentFactory.teacher(undervisningsforholdResource, teacher.get(), basisGroup, school.get()));
                                }
                            });

                    undervisningsforholdResource.getUndervisningsgruppe()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getTeachingGroups()::get)
                            .filter(Objects::nonNull)
                            .forEach(teachingGroup -> {
                                if (teacher.isPresent() && school.isPresent()) {
                                    enrollments.add(EnrollmentFactory.teacher(undervisningsforholdResource, teacher.get(), teachingGroup, school.get()));
                                }
                            });
                });

        return enrollments;
    }

    public Enrollment getEnrollment(String sourcedId) {
        return getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<Enrollment> getEnrollmentsForSchool(String sourcedId) {
        return getAllEnrollments()
                .stream()
                .filter(enrollment -> enrollment.getSchool().getSourcedId().equals(sourcedId))
                .collect(Collectors.toList());
    }
}
