package no.fint.oneroster.repository;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.elev.*;
import no.fint.model.resource.utdanning.timeplan.FagResource;
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
        try {
            return (Org) resources.get(sourcedId);
        } catch (ClassCastException ex) {
            log.warn(sourcedId, ex);
            return null;
        }
    }

    public List<Clazz> getClazzes() {
        return getResourcesByType(Clazz.class);
    }

    public Clazz getClazzById(String sourcedId) {
        try {
            return (Clazz) resources.get(sourcedId);
        } catch (ClassCastException ex) {
            log.warn(sourcedId, ex);
            return null;
        }
    }

    public List<Course> getCourses() {
        return getResourcesByType(Course.class);
    }

    public Course getCourseById(String sourcedId) {
        try {
            return (Course) resources.get(sourcedId);
        } catch (ClassCastException ex) {
            log.warn(sourcedId, ex);
            return null;
        }
    }

    public List<Enrollment> getEnrollments() {
        return getResourcesByType(Enrollment.class);
    }

    public Enrollment getEnrollmentById(String sourcedId) {
        try {
            return (Enrollment) resources.get(sourcedId);
        } catch (ClassCastException ex) {
            log.warn(sourcedId, ex);
            return null;
        }
    }

    public List<User> getUsers() {
        return getResourcesByType(User.class);
    }

    public User getUserById(String sourcedId) {
        try {
            return (User) resources.get(sourcedId);
        } catch (ClassCastException ex) {
            log.warn(sourcedId, ex);
            return null;
        }
    }

    private void updateBasisGroups(SkoleResource schoolResource) {
        List<AcademicSession> terms = academicSessionService.getAllTerms();

        OneRosterProperties.Org org = oneRosterProperties.getOrg();

        schoolResource.getBasisgruppe()
                .stream()
                .map(Link::getHref)
                .map(String::toLowerCase)
                .map(fintService::getBasisGroupById)
                .filter(Objects::nonNull)
                .forEach(basisGroup -> {
                    Optional<ArstrinnResource> level = basisGroup.getTrinn()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintService::getLevelById)
                            .filter(Objects::nonNull)
                            .findAny();

                    if (level.isPresent() && !terms.isEmpty()) {
                        Clazz clazz = clazzFactory.basisGroup(basisGroup, level.get(), schoolResource, terms);
                        resources.put(clazz.getSourcedId(), clazz);

                        Course course = CourseFactory.level(level.get(), org);
                        resources.putIfAbsent(course.getSourcedId(), course);
                    }

                    basisGroup.getElevforhold()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintService::getStudentRelationById)
                            .filter(Objects::nonNull)
                            .forEach(studentRelation -> {
                                Optional<ElevResource> student = getStudent(studentRelation);

                                student.ifPresent(s -> {
                                    Enrollment enrollment = EnrollmentFactory.student(studentRelation, s, basisGroup, schoolResource);
                                    resources.put(enrollment.getSourcedId(), enrollment);
                                });
                            });

                    basisGroup.getUndervisningsforhold()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintService::getTeachingRelationById)
                            .filter(Objects::nonNull)
                            .forEach(teachingRelation -> {
                                Optional<SkoleressursResource> teacher = getTeacher(teachingRelation);

                                teacher.ifPresent(t -> {
                                    Enrollment enrollment = EnrollmentFactory.teacher(teachingRelation, t, basisGroup, schoolResource);
                                    resources.put(enrollment.getSourcedId(), enrollment);
                                });
                            });
                });
    }

    private void updateTeachingGroups(SkoleResource schoolResource) {
        List<AcademicSession> terms = academicSessionService.getAllTerms();

        OneRosterProperties.Org org = oneRosterProperties.getOrg();

        schoolResource.getUndervisningsgruppe()
                .stream()
                .map(Link::getHref)
                .map(String::toLowerCase)
                .map(fintService::getTeachingGroupById)
                .filter(Objects::nonNull)
                .forEach(teachingGroup -> {
                    Optional<FagResource> subject = teachingGroup.getFag()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintService::getSubjectById)
                            .filter(Objects::nonNull)
                            .findFirst();

                    if (subject.isPresent() && !terms.isEmpty()) {
                        Clazz clazz = clazzFactory.teachingGroup(teachingGroup, subject.get(), schoolResource, terms);
                        resources.put(clazz.getSourcedId(), clazz);

                        Course course = CourseFactory.subject(subject.get(), org);
                        resources.putIfAbsent(course.getSourcedId(), course);
                    }

                    teachingGroup.getElevforhold()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintService::getStudentRelationById)
                            .filter(Objects::nonNull)
                            .forEach(studentRelation -> {
                                Optional<ElevResource> student = getStudent(studentRelation);

                                student.ifPresent(s -> {
                                    Enrollment enrollment = EnrollmentFactory.student(studentRelation, s, teachingGroup, schoolResource);
                                    resources.put(enrollment.getSourcedId(), enrollment);
                                });
                            });

                    teachingGroup.getUndervisningsforhold()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintService::getTeachingRelationById)
                            .filter(Objects::nonNull)
                            .forEach(teachingRelation -> {
                                Optional<SkoleressursResource> teacher = getTeacher(teachingRelation);

                                teacher.ifPresent(t -> {
                                    Enrollment enrollment = EnrollmentFactory.teacher(teachingRelation, t, teachingGroup, schoolResource);
                                    resources.put(enrollment.getSourcedId(), enrollment);
                                });
                            });
                });
    }

    private void updateContactTeacherGroups(SkoleResource schoolResource) {
        List<AcademicSession> terms = academicSessionService.getAllTerms();

        schoolResource.getKontaktlarergruppe()
                .stream()
                .map(Link::getHref)
                .map(String::toLowerCase)
                .map(fintService::getContactTeacherGroupById)
                .filter(Objects::nonNull)
                .forEach(contactTeacherGroup -> {
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

                    if (level.isPresent() && !terms.isEmpty()) {
                        Clazz clazz = clazzFactory.contactTeacherGroup(contactTeacherGroup, level.get(), schoolResource, terms);
                        resources.put(clazz.getSourcedId(), clazz);
                    }

                    contactTeacherGroup.getElevforhold()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintService::getStudentRelationById)
                            .filter(Objects::nonNull)
                            .forEach(studentRelation -> {
                                Optional<ElevResource> student = getStudent(studentRelation);

                                student.ifPresent(s -> {
                                    Enrollment enrollment = EnrollmentFactory.student(studentRelation, s, contactTeacherGroup, schoolResource);
                                    resources.put(enrollment.getSourcedId(), enrollment);
                                });
                            });

                    contactTeacherGroup.getUndervisningsforhold()
                            .stream()
                            .map(Link::getHref)
                            .map(String::toLowerCase)
                            .map(fintService::getTeachingRelationById)
                            .filter(Objects::nonNull)
                            .forEach(teachingRelation -> {
                                Optional<SkoleressursResource> teacher = getTeacher(teachingRelation);

                                teacher.ifPresent(t -> {
                                    Enrollment enrollment = EnrollmentFactory.teacher(teachingRelation, t, contactTeacherGroup, schoolResource);
                                    resources.put(enrollment.getSourcedId(), enrollment);
                                });
                            });
                });
    }

    private Optional<SkoleressursResource> getTeacher(UndervisningsforholdResource teachingRelation) {
        return teachingRelation.getSkoleressurs()
                .stream()
                .map(Link::getHref)
                .map(String::toLowerCase)
                .map(fintService::getTeacherById)
                .filter(Objects::nonNull)
                .findAny();
    }

    private Optional<ElevResource> getStudent(ElevforholdResource studentRelation) {
        return studentRelation.getElev()
                .stream()
                .map(Link::getHref)
                .map(String::toLowerCase)
                .map(fintService::getStudentById)
                .filter(Objects::nonNull)
                .findAny();
    }

    private void updateStudents() {
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
    }

    private void updateTeachers() {
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
        synchronized (resources) {
            return resources.values()
                    .stream()
                    .filter(clazz::isInstance)
                    .map(clazz::cast)
                    .sorted(Comparator.comparing(T::getSourcedId))
                    .collect(Collectors.toList());
        }
    }

    private void update() {
        OneRosterProperties.Org org = oneRosterProperties.getOrg();

        Org schoolOwner = OrgFactory.schoolOwner(org);

        fintService.getSchools()
                .stream()
                .peek(this::updateBasisGroups)
                .peek(this::updateTeachingGroups)
                .peek(schoolResource -> {
                    if (oneRosterProperties.getProfile().isContactTeacherGroups()) {
                        updateContactTeacherGroups(schoolResource);
                    }
                })
                .map(schoolResource -> {
                    Org school = OrgFactory.school(schoolResource);

                    if (schoolOwner.getChildren() == null) {
                        schoolOwner.setChildren(new ArrayList<>());
                    }

                    school.setParent(GUIDRef.of(GUIDType.ORG, schoolOwner.getSourcedId()));
                    schoolOwner.getChildren().add(GUIDRef.of(GUIDType.ORG, school.getSourcedId()));

                    return school;
                })
                .forEach(school -> resources.put(school.getSourcedId(), school));

        resources.put(schoolOwner.getSourcedId(), schoolOwner);

        this.updateStudents();
        this.updateTeachers();
    }

    public void updateResources() {
        synchronized (resources) {
            resources.clear();
            update();
        }
        log.info("Update complete");
    }
}
