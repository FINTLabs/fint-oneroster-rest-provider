package no.fint.oneroster.repository;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
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
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
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

    private final ConcurrentMap<String, Base> cache = new ConcurrentSkipListMap<>();

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
        return getResourceByTypeAndId(Org.class, sourcedId);
    }

    public List<Clazz> getClazzes() {
        return getResourcesByType(Clazz.class);
    }

    public Clazz getClazzById(String sourcedId) {
        return getResourceByTypeAndId(Clazz.class, sourcedId);
    }

    public List<Course> getCourses() {
        return getResourcesByType(Course.class);
    }

    public Course getCourseById(String sourcedId) {
        return getResourceByTypeAndId(Course.class, sourcedId);
    }

    public List<Enrollment> getEnrollments() {
        return getResourcesByType(Enrollment.class);
    }

    public Enrollment getEnrollmentById(String sourcedId) {
        return getResourceByTypeAndId(Enrollment.class, sourcedId);
    }

    public List<User> getUsers() {
        return getResourcesByType(User.class);
    }

    public User getUserById(String sourcedId) {
        return getResourceByTypeAndId(User.class, sourcedId);
    }

    private BiConsumer<SkoleResource, Map<String, Base>> updateBasisGroups() {
        return (schoolResource, resources) -> {

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
                            resources.put(course.getSourcedId(), course);

                            basisGroup.getElevforhold()
                                    .stream()
                                    .map(Link::getHref)
                                    .map(String::toLowerCase)
                                    .map(fintService::getStudentRelationById)
                                    .filter(Objects::nonNull)
                                    .forEach(studentRelation -> getStudent(studentRelation).ifPresent(student -> {
                                        Enrollment enrollment = EnrollmentFactory.student(studentRelation, student, basisGroup, schoolResource);
                                        resources.put(enrollment.getSourcedId(), enrollment);
                                    }));

                            basisGroup.getUndervisningsforhold()
                                    .stream()
                                    .map(Link::getHref)
                                    .map(String::toLowerCase)
                                    .map(fintService::getTeachingRelationById)
                                    .filter(Objects::nonNull)
                                    .forEach(teachingRelation -> getTeacher(teachingRelation).ifPresent(teacher -> {
                                        Enrollment enrollment = EnrollmentFactory.teacher(teachingRelation, teacher, basisGroup, schoolResource);
                                        resources.put(enrollment.getSourcedId(), enrollment);
                                    }));
                        }
                    });
        };
    }

    private BiConsumer<SkoleResource, Map<String, Base>> updateTeachingGroups() {
        return (schoolResource, resources) -> {
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
                            resources.put(course.getSourcedId(), course);

                            teachingGroup.getElevforhold()
                                    .stream()
                                    .map(Link::getHref)
                                    .map(String::toLowerCase)
                                    .map(fintService::getStudentRelationById)
                                    .filter(Objects::nonNull)
                                    .forEach(studentRelation -> getStudent(studentRelation).ifPresent(student -> {
                                        Enrollment enrollment = EnrollmentFactory.student(studentRelation, student, teachingGroup, schoolResource);
                                        resources.put(enrollment.getSourcedId(), enrollment);
                                    }));

                            teachingGroup.getUndervisningsforhold()
                                    .stream()
                                    .map(Link::getHref)
                                    .map(String::toLowerCase)
                                    .map(fintService::getTeachingRelationById)
                                    .filter(Objects::nonNull)
                                    .forEach(teachingRelation -> getTeacher(teachingRelation).ifPresent(teacher -> {
                                        Enrollment enrollment = EnrollmentFactory.teacher(teachingRelation, teacher, teachingGroup, schoolResource);
                                        resources.put(enrollment.getSourcedId(), enrollment);
                                    }));
                        }
                    });
        };
    }

    private BiConsumer<SkoleResource, Map<String, Base>> updateContactTeacherGroups() {
        return (schoolResource, resources) -> {
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

                            contactTeacherGroup.getElevforhold()
                                    .stream()
                                    .map(Link::getHref)
                                    .map(String::toLowerCase)
                                    .map(fintService::getStudentRelationById)
                                    .filter(Objects::nonNull)
                                    .forEach(studentRelation -> getStudent(studentRelation).ifPresent(student -> {
                                        Enrollment enrollment = EnrollmentFactory.student(studentRelation, student, contactTeacherGroup, schoolResource);
                                        resources.put(enrollment.getSourcedId(), enrollment);
                                    }));

                            contactTeacherGroup.getUndervisningsforhold()
                                    .stream()
                                    .map(Link::getHref)
                                    .map(String::toLowerCase)
                                    .map(fintService::getTeachingRelationById)
                                    .filter(Objects::nonNull)
                                    .forEach(teachingRelation -> getTeacher(teachingRelation).ifPresent(teacher -> {
                                        Enrollment enrollment = EnrollmentFactory.teacher(teachingRelation, teacher, contactTeacherGroup, schoolResource);
                                        resources.put(enrollment.getSourcedId(), enrollment);
                                    }));
                        }
                    });
        };
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

    private Consumer<Map<String, Base>> updateStudents() {
        return resources -> fintService.getStudents().forEach(student -> {
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

    private Consumer<Map<String, Base>> updateTeachers() {
        return resources -> fintService.getTeachers().forEach(teacher -> {
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

    private BiConsumer<SkoleResource, Map<String, Base>> updateSchools(Org schoolOwner) {
        return (schoolResource, resources) -> {
            Org school = OrgFactory.school(schoolResource);

            if (schoolOwner.getChildren() == null) {
                schoolOwner.setChildren(new ArrayList<>());
            }

            school.setParent(GUIDRef.of(GUIDType.ORG, schoolOwner.getSourcedId()));
            schoolOwner.getChildren().add(GUIDRef.of(GUIDType.ORG, school.getSourcedId()));

            resources.put(school.getSourcedId(), school);
        };
    }

    public <T extends Base> List<T> getResourcesByType(Class<T> clazz) {
        return cache.values()
                .stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    private <T extends Base> T getResourceByTypeAndId(Class<T> clazz, String sourcedId) {
        try {
            return clazz.cast(cache.get(sourcedId));
        } catch (ClassCastException ex) {
            log.warn(sourcedId, ex);
            return null;
        }
    }

    public void update() {
        Map<String, Base> resources = new HashMap<>();

        Org schoolOwner = OrgFactory.schoolOwner(oneRosterProperties.getOrg());

        fintService.getSchools().forEach(schoolResource -> {
            updateSchools(schoolOwner).accept(schoolResource, resources);
            updateBasisGroups().accept(schoolResource, resources);
            updateTeachingGroups().accept(schoolResource, resources);

            if (oneRosterProperties.getProfile().isContactTeacherGroups()) {
                updateContactTeacherGroups().accept(schoolResource, resources);
            }
        });

        resources.put(schoolOwner.getSourcedId(), schoolOwner);

        updateStudents().accept(resources);
        updateTeachers().accept(resources);

        updateCache().accept(resources);
    }

    private Consumer<Map<String, Base>> updateCache() {
        return resources -> {
            MapDifference<String, Base> difference = Maps.difference(resources, cache);

            if (difference.areEqual()) {
                return;
            }

            difference.entriesOnlyOnLeft().forEach(cache::put);
            difference.entriesOnlyOnRight().forEach(cache::remove);
            difference.entriesDiffering().forEach((key, value) -> cache.put(key, value.leftValue()));

            log.info("Added {}, removed {}, replaced {} entries in cache", difference.entriesOnlyOnLeft().size(),
                    difference.entriesOnlyOnRight().size(), difference.entriesDiffering().size());
        };
    }

    private final Predicate<UndervisningsforholdResource> isTeacher = teachingRelation ->
            !teachingRelation.getBasisgruppe().isEmpty() ||
                    !teachingRelation.getUndervisningsgruppe().isEmpty() ||
                    !teachingRelation.getKontaktlarergruppe().isEmpty();
}
