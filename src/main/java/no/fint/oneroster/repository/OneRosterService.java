package no.fint.oneroster.repository;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.elev.*;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.factory.CourseFactory;
import no.fint.oneroster.factory.EnrollmentFactory;
import no.fint.oneroster.factory.OrgFactory;
import no.fint.oneroster.factory.clazz.ClazzFactory;
import no.fint.oneroster.factory.user.UserFactory;
import no.fint.oneroster.model.*;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.properties.OneRosterProperties;
import no.fint.oneroster.service.AcademicSessionService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OneRosterService {
    private final FintEducationService fintEducationService;
    private final FintAdministrationService fintAdministrationService;
    private final OneRosterProperties oneRosterProperties;
    private final AcademicSessionService academicSessionService;
    private final ClazzFactory clazzFactory;
    private final UserFactory userFactory;

    public OneRosterService(FintEducationService fintEducationService, FintAdministrationService fintAdministrationService, OneRosterProperties oneRosterProperties, AcademicSessionService academicSessionService, ClazzFactory clazzFactory, UserFactory userFactory) {
        this.fintEducationService = fintEducationService;
        this.fintAdministrationService = fintAdministrationService;
        this.oneRosterProperties = oneRosterProperties;
        this.academicSessionService = academicSessionService;
        this.clazzFactory = clazzFactory;
        this.userFactory = userFactory;
    }

    @Cacheable(value = "orgs")
    public List<Org> getAllOrgs() {
        Org schoolOwner = OrgFactory.schoolOwner(oneRosterProperties.getOrg());

        List<Org> orgs = fintEducationService.getSchools()
                .values()
                .stream()
                .distinct()
                .map(OrgFactory::school)
                .peek(school -> {
                    if (schoolOwner.getChildren() == null) {
                        schoolOwner.setChildren(new ArrayList<>());
                    }
                    school.setParent(GUIDRef.of(GUIDType.ORG, schoolOwner.getSourcedId()));
                    schoolOwner.getChildren().add(GUIDRef.of(GUIDType.ORG, school.getSourcedId()));
                })
                .collect(Collectors.toList());

        orgs.add(schoolOwner);

        orgs.sort(Comparator.comparing(Org::getSourcedId));

        return orgs;
    }

    @Cacheable(value = "clazzes")
    public List<Clazz> getAllClazzes() {
        List<AcademicSession> terms = academicSessionService.getAllTerms();

        List<Clazz> clazzes = new ArrayList<>();

        fintEducationService.getBasisGroups()
                .values()
                .stream()
                .distinct()
                .forEach(basisGroup -> {
                    Optional<ArstrinnResource> level = basisGroup.getTrinn()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getLevels()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    Optional<SkoleResource> school = basisGroup.getSkole()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getSchools()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    if (level.isPresent() && school.isPresent() && !terms.isEmpty()) {
                        clazzes.add(clazzFactory.basisGroup(basisGroup, level.get(), school.get(), terms));
                    }
                });

        fintEducationService.getTeachingGroups()
                .values()
                .stream()
                .distinct()
                .forEach(teachingGroup -> {
                    Optional<FagResource> subject = teachingGroup.getFag()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getSubjects()::get)
                            .filter(Objects::nonNull)
                            .findFirst();

                    Optional<SkoleResource> school = teachingGroup.getSkole()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getSchools()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    if (subject.isPresent() && school.isPresent() && !terms.isEmpty()) {
                        clazzes.add(clazzFactory.teachingGroup(teachingGroup, subject.get(), school.get(), terms));
                    }
                });

        clazzes.sort(Comparator.comparing(Clazz::getSourcedId));

        return clazzes;
    }

    @Cacheable(value = "courses")
    public List<Course> getAllCourses() {
        OneRosterProperties.Org org = oneRosterProperties.getOrg();

        List<Course> courses = new ArrayList<>();

        fintEducationService.getTeachingGroups()
                .values()
                .stream()
                .distinct()
                .map(UndervisningsgruppeResource::getFag)
                .flatMap(List::stream)
                .map(Link::getHref)
                .map(String::toLowerCase)
                .map(fintEducationService.getSubjects()::get)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(subject -> courses.add(CourseFactory.subject(subject, org)));

        fintEducationService.getBasisGroups()
                .values()
                .stream()
                .distinct()
                .map(BasisgruppeResource::getTrinn)
                .flatMap(List::stream)
                .map(Link::getHref)
                .map(String::toLowerCase)
                .map(fintEducationService.getLevels()::get)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(level -> courses.add(CourseFactory.level(level, org)));

        courses.sort(Comparator.comparing(Course::getSourcedId));

        return courses;
    }

    @Cacheable(value = "enrollments")
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

        enrollments.sort(Comparator.comparing(Enrollment::getSourcedId));

        return enrollments;
    }

    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        fintEducationService.getStudents()
                .values()
                .stream()
                .distinct()
                .forEach(student -> {
                    Optional<PersonResource> person = student.getPerson()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getPersons()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    List<SkoleResource> schoolResources = student.getElevforhold()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getStudentRelations()::get)
                            .filter(Objects::nonNull)
                            .map(ElevforholdResource::getSkole)
                            .flatMap(List::stream)
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getSchools()::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (person.isPresent() && !schoolResources.isEmpty()) {
                        users.add(userFactory.student(student, person.get(), schoolResources));
                    }
                });

        fintEducationService.getTeachers()
                .values()
                .stream()
                .distinct()
                .forEach(teacher -> {
                    Optional<PersonalressursResource> personnelResource = teacher.getPersonalressurs()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintAdministrationService.getPersonnel()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    Optional<PersonResource> personResource = personnelResource
                            .map(PersonalressursResource::getPerson)
                            .orElseGet(Collections::emptyList)
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintAdministrationService.getPersons()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    List<SkoleResource> schoolResources = teacher.getUndervisningsforhold()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getTeachingRelations()::get)
                            .filter(Objects::nonNull)
                            .map(UndervisningsforholdResource::getSkole)
                            .flatMap(List::stream)
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintEducationService.getSchools()::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (personnelResource.isPresent() && personResource.isPresent() && !schoolResources.isEmpty()) {
                        users.add(userFactory.teacher(teacher, personnelResource.get(), personResource.get(), schoolResources));
                    }
                });

        users.sort(Comparator.comparing(User::getSourcedId));

        return users;
    }
}
