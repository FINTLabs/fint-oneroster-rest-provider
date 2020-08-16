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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OneRosterService {
    private final OneRosterProperties oneRosterProperties;
    private final AcademicSessionService academicSessionService;
    private final ClazzFactory clazzFactory;
    private final UserFactory userFactory;
    private final FintService fintService;

    private final Map<String, ? super Base> resources = Collections.synchronizedMap(new HashMap<>());

    public OneRosterService(OneRosterProperties oneRosterProperties, AcademicSessionService academicSessionService, ClazzFactory clazzFactory, UserFactory userFactory, FintService fintService) {
        this.oneRosterProperties = oneRosterProperties;
        this.academicSessionService = academicSessionService;
        this.clazzFactory = clazzFactory;
        this.userFactory = userFactory;
        this.fintService = fintService;
    }

    public List<Org> getOrgs() {
        return getResourcesByType(Org.class);
    }

    public Org getOrgById(String sourcedId) {
        return (Org) resources.get(sourcedId);
    }

    private void updateOrgs() {
        Org schoolOwner = OrgFactory.schoolOwner(oneRosterProperties.getOrg());

        fintService.getSchools()
                .stream()
                .map(OrgFactory::school)
                .peek(school -> {
                    if (schoolOwner.getChildren() == null) {
                        schoolOwner.setChildren(new ArrayList<>());
                    }
                    school.setParent(GUIDRef.of(GUIDType.ORG, schoolOwner.getSourcedId()));
                    schoolOwner.getChildren().add(GUIDRef.of(GUIDType.ORG, school.getSourcedId()));
                })
                .forEach(school -> resources.put(school.getSourcedId(), school));

        resources.put(schoolOwner.getSourcedId(), schoolOwner);
    }

    public List<Clazz> getClazzes() {
        return getResourcesByType(Clazz.class);
    }

    public Clazz getClazzById(String sourcedId) {
        return (Clazz) resources.get(sourcedId);
    }

    private void updateClazzes() {
        List<AcademicSession> terms = academicSessionService.getAllTerms();

        fintService.getBasisGroups().forEach(basisGroup -> {
            Optional<ArstrinnResource> level = basisGroup.getTrinn()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getLevelById)
                    .filter(Objects::nonNull)
                    .findAny();

            Optional<SkoleResource> school = basisGroup.getSkole()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getSchoolById)
                    .filter(Objects::nonNull)
                    .findAny();

            if (level.isPresent() && school.isPresent() && !terms.isEmpty()) {
                Clazz clazz = clazzFactory.basisGroup(basisGroup, level.get(), school.get(), terms);
                resources.put(clazz.getSourcedId(), clazz);
            }
        });

        fintService.getTeachingGroups().forEach(teachingGroup -> {
            Optional<FagResource> subject = teachingGroup.getFag()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getSubjectById)
                    .filter(Objects::nonNull)
                    .findFirst();

            Optional<SkoleResource> school = teachingGroup.getSkole()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getSchoolById)
                    .filter(Objects::nonNull)
                    .findAny();

            if (subject.isPresent() && school.isPresent() && !terms.isEmpty()) {
                Clazz clazz = clazzFactory.teachingGroup(teachingGroup, subject.get(), school.get(), terms);
                resources.put(clazz.getSourcedId(), clazz);
            }
        });

        if (oneRosterProperties.getProfile().isContactTeacherGroups()) {
            fintService.getContactTeacherGroups().forEach(contactTeacherGroup -> {
                Optional<ArstrinnResource> level = contactTeacherGroup.getBasisgruppe()
                        .stream()
                        .findFirst()
                        .map(Link::getHref)
                        .map(String::toLowerCase)
                        .map(fintService::getBasisGroupById)
                        .map(BasisgruppeResource::getTrinn)
                        .orElseGet(Collections::emptyList)
                        .stream()
                        .map(Link::getHref)
                        .map(String::toLowerCase)
                        .map(fintService::getLevelById)
                        .filter(Objects::nonNull)
                        .findAny();

                Optional<SkoleResource> school = contactTeacherGroup.getSkole()
                        .stream()
                        .map(Link::getHref)
                        .map(String::toLowerCase)
                        .map(fintService::getSchoolById)
                        .filter(Objects::nonNull)
                        .findAny();

                if (level.isPresent() && school.isPresent() && !terms.isEmpty()) {
                    Clazz clazz = clazzFactory.contactTeacherGroup(contactTeacherGroup, level.get(), school.get(), terms);
                    resources.put(clazz.getSourcedId(), clazz);
                }
            });
        }
    }

    public List<Course> getCourses() {
        return getResourcesByType(Course.class);
    }

    public Course getCourseById(String sourcedId) {
        return (Course) resources.get(sourcedId);
    }

    private void updateCourses() {
        OneRosterProperties.Org org = oneRosterProperties.getOrg();

        fintService.getTeachingGroups()
                .stream()
                .map(UndervisningsgruppeResource::getFag)
                .flatMap(List::stream)
                .map(Link::getHref)
                .map(String::toLowerCase)
                .map(fintService::getSubjectById)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(subject -> {
                    Course course = CourseFactory.subject(subject, org);
                    resources.put(course.getSourcedId(), course);
                });

        fintService.getBasisGroups()
                .stream()
                .map(BasisgruppeResource::getTrinn)
                .flatMap(List::stream)
                .map(Link::getHref)
                .map(String::toLowerCase)
                .map(fintService::getLevelById)
                .filter(Objects::nonNull)
                .distinct()
                .forEach(level -> {
                    Course course = CourseFactory.level(level, org);
                    resources.put(course.getSourcedId(), course);
                });
    }

    public List<Enrollment> getEnrollments() {
        return getResourcesByType(Enrollment.class);
    }

    public Enrollment getEnrollmentById(String sourcedId) {
        return (Enrollment) resources.get(sourcedId);
    }

    private void updateEnrollments() {
        fintService.getStudentRelations().forEach(elevforholdResource -> {
            Optional<ElevResource> student = elevforholdResource.getElev()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getStudentById)
                    .filter(Objects::nonNull)
                    .findAny();

            Optional<SkoleResource> school = elevforholdResource.getSkole()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getSchoolById)
                    .filter(Objects::nonNull)
                    .findAny();

            elevforholdResource.getBasisgruppe()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getBasisGroupById)
                    .filter(Objects::nonNull)
                    .forEach(basisGroup -> {
                        if (student.isPresent() && school.isPresent()) {
                            Enrollment enrollment = EnrollmentFactory.student(elevforholdResource, student.get(), basisGroup, school.get());
                            resources.put(enrollment.getSourcedId(), enrollment);
                        }
                    });

            elevforholdResource.getUndervisningsgruppe()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getTeachingGroupById)
                    .filter(Objects::nonNull)
                    .forEach(teachingGroup -> {
                        if (student.isPresent() && school.isPresent()) {
                            Enrollment enrollment = EnrollmentFactory.student(elevforholdResource, student.get(), teachingGroup, school.get());
                            resources.put(enrollment.getSourcedId(), enrollment);
                        }
                    });

            if (oneRosterProperties.getProfile().isContactTeacherGroups()) {
                elevforholdResource.getKontaktlarergruppe()
                        .stream()
                        .map(Link::getHref)
                        .map(String::toLowerCase)
                        .map(fintService::getContactTeacherGroupById)
                        .filter(Objects::nonNull)
                        .forEach(contactTeacherGroup -> {
                            if (student.isPresent() && school.isPresent()) {
                                Enrollment enrollment = EnrollmentFactory.student(elevforholdResource, student.get(), contactTeacherGroup, school.get());
                                resources.put(enrollment.getSourcedId(), enrollment);
                            }
                        });
            }
        });

        fintService.getTeachingRelations().forEach(undervisningsforholdResource -> {
            Optional<SkoleressursResource> teacher = undervisningsforholdResource.getSkoleressurs()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getTeacherById)
                    .filter(Objects::nonNull)
                    .findAny();

            Optional<SkoleResource> school = undervisningsforholdResource.getSkole()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getSchoolById)
                    .filter(Objects::nonNull)
                    .findAny();

            undervisningsforholdResource.getBasisgruppe()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getBasisGroupById)
                    .filter(Objects::nonNull)
                    .forEach(basisGroup -> {
                        if (teacher.isPresent() && school.isPresent()) {
                            Enrollment enrollment = EnrollmentFactory.teacher(undervisningsforholdResource, teacher.get(), basisGroup, school.get());
                            resources.put(enrollment.getSourcedId(), enrollment);
                        }
                    });

            undervisningsforholdResource.getUndervisningsgruppe()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getTeachingGroupById)
                    .filter(Objects::nonNull)
                    .forEach(teachingGroup -> {
                        if (teacher.isPresent() && school.isPresent()) {
                            Enrollment enrollment = EnrollmentFactory.teacher(undervisningsforholdResource, teacher.get(), teachingGroup, school.get());
                            resources.put(enrollment.getSourcedId(), enrollment);
                        }
                    });

            if (oneRosterProperties.getProfile().isContactTeacherGroups()) {
                undervisningsforholdResource.getKontaktlarergruppe()
                        .stream()
                        .map(Link::getHref)
                        .map(String::toLowerCase)
                        .map(fintService::getContactTeacherGroupById)
                        .filter(Objects::nonNull)
                        .forEach(contactTeacherGroup -> {
                            if (teacher.isPresent() && school.isPresent()) {
                                Enrollment enrollment = EnrollmentFactory.teacher(undervisningsforholdResource, teacher.get(), contactTeacherGroup, school.get());
                                resources.put(enrollment.getSourcedId(), enrollment);
                            }
                        });
            }
        });
    }

    public List<User> getUsers() {
        return getResourcesByType(User.class);
    }

    public User getUserById(String sourcedId) {
        return (User) resources.get(sourcedId);
    }

    private void updateUsers() {
        fintService.getStudents().forEach(student -> {
            Optional<PersonResource> person = student.getPerson()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getPersonById)
                    .filter(Objects::nonNull)
                    .findAny();

            List<SkoleResource> schoolResources = student.getElevforhold()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getStudentRelationById)
                    .filter(Objects::nonNull)
                    .map(ElevforholdResource::getSkole)
                    .flatMap(List::stream)
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getSchoolById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (person.isPresent() && !schoolResources.isEmpty()) {
                User user = userFactory.student(student, person.get(), schoolResources);
                resources.put(user.getSourcedId(), user);
            }
        });

        fintService.getTeachers().forEach(teacher -> {
            Optional<PersonalressursResource> personnelResource = teacher.getPersonalressurs()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getPersonnelById)
                    .filter(Objects::nonNull)
                    .findAny();

            Optional<PersonResource> personResource = personnelResource
                    .map(PersonalressursResource::getPerson)
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getPersonById)
                    .filter(Objects::nonNull)
                    .findAny();

            List<SkoleResource> schoolResources = teacher.getUndervisningsforhold()
                    .stream()
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getTeachingRelationById)
                    .filter(Objects::nonNull)
                    .filter(isTeacher)
                    .map(UndervisningsforholdResource::getSkole)
                    .flatMap(List::stream)
                    .map(Link::getHref)
                    .map(String::toLowerCase)
                    .map(fintService::getSchoolById)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (personnelResource.isPresent() && personResource.isPresent() && !schoolResources.isEmpty()) {
                User user = userFactory.teacher(teacher, personnelResource.get(), personResource.get(), schoolResources);
                resources.put(user.getSourcedId(), user);
            }
        });
    }

    private final Predicate<UndervisningsforholdResource> isTeacher = teachingRelation ->
            !teachingRelation.getBasisgruppe().isEmpty() ||
                    !teachingRelation.getUndervisningsgruppe().isEmpty() ||
                    !teachingRelation.getKontaktlarergruppe().isEmpty();

    public <T extends Base> List<T> getResourcesByType(Class<T> clazz) {
        return resources.values()
                .stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .sorted(Comparator.comparing(T::getSourcedId))
                .collect(Collectors.toList());
    }

    public void updateResources() {
        synchronized (resources) {
            resources.clear();
            updateOrgs();
            updateClazzes();
            updateCourses();
            updateEnrollments();
            updateUsers();
        }
        log.info("Update complete");
    }
}
