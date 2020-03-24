package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.EnrollmentFactory;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.repository.FintRepository;
import no.fint.oneroster.util.LinkUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EnrollmentService {

    private final FintRepository fintRepository;

    public EnrollmentService(FintRepository fintRepository) {
        this.fintRepository = fintRepository;
    }

    public List<Enrollment> getAllEnrollments(String orgId) {
        Map<String, SkoleResource> schools = fintRepository.getSchools(orgId);
        Map<String, BasisgruppeResource> basisGroups = fintRepository.getBasisGroups(orgId);
        Map<String, UndervisningsgruppeResource> teachingGroups = fintRepository.getTeachingGroups(orgId);

        List<Enrollment> enrollments = new ArrayList<>();

        Map<String, ElevResource> students = fintRepository.getStudents(orgId);

        fintRepository.getStudentRelations(orgId)
                .values()
                .forEach(elevforholdResource -> {
                    Optional<ElevResource> student = elevforholdResource.getElev()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(students::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    Optional<SkoleResource> school = elevforholdResource.getSkole()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(schools::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    elevforholdResource.getBasisgruppe()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(basisGroups::get)
                            .filter(Objects::nonNull)
                            .forEach(basisGroup -> {
                                if (student.isPresent() && school.isPresent()) {
                                    enrollments.add(EnrollmentFactory.student(elevforholdResource, student.get(), basisGroup, school.get()));
                                }
                            });

                    elevforholdResource.getUndervisningsgruppe()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(teachingGroups::get)
                            .filter(Objects::nonNull)
                            .forEach(teachingGroup -> {
                                if (student.isPresent() && school.isPresent()) {
                                    enrollments.add(EnrollmentFactory.student(elevforholdResource, student.get(), teachingGroup, school.get()));
                                }
                            });
                });

        Map<String, SkoleressursResource> schoolResourceMap = fintRepository.getTeachers(orgId);

        fintRepository.getTeachingRelations(orgId)
                .values()
                .forEach(undervisningsforholdResource -> {
                    Optional<SkoleressursResource> teacher = undervisningsforholdResource.getSkoleressurs()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(schoolResourceMap::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    Optional<SkoleResource> school = undervisningsforholdResource.getSkole()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(schools::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    undervisningsforholdResource.getBasisgruppe()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(basisGroups::get)
                            .filter(Objects::nonNull)
                            .forEach(basisGroup -> {
                                if (teacher.isPresent() && school.isPresent()) {
                                    enrollments.add(EnrollmentFactory.teacher(undervisningsforholdResource, teacher.get(), basisGroup, school.get()));
                                }
                            });

                    undervisningsforholdResource.getUndervisningsgruppe()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(teachingGroups::get)
                            .filter(Objects::nonNull)
                            .forEach(teachingGroup -> {
                                if (teacher.isPresent() && school.isPresent()) {
                                    enrollments.add(EnrollmentFactory.teacher(undervisningsforholdResource, teacher.get(), teachingGroup, school.get()));
                                }
                            });
                });

        return enrollments;
    }

    public Enrollment getEnrollment(String orgId, String sourcedId) {
        return getAllEnrollments(orgId).stream()
                .filter(enrollment -> enrollment.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<Enrollment> getEnrollmentsForSchool(String orgId, String sourcedId) {
        return getAllEnrollments(orgId)
                .stream()
                .filter(enrollment -> enrollment.getSchool().getSourcedId().equals(sourcedId))
                .collect(Collectors.toList());
    }
}
