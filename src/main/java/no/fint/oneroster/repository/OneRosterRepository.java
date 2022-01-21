package no.fint.oneroster.repository;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.basisklasser.GruppeResource;
import no.fint.model.resource.utdanning.elev.*;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.fint.model.resource.utdanning.kodeverk.TerminResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.factory.AcademicSessionFactory;
import no.fint.oneroster.factory.CourseFactory;
import no.fint.oneroster.factory.EnrollmentFactory;
import no.fint.oneroster.factory.OrgFactory;
import no.fint.oneroster.factory.clazz.ClazzFactory;
import no.fint.oneroster.factory.user.UserFactory;
import no.fint.oneroster.model.*;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.properties.OneRosterProperties;
import no.fint.oneroster.util.PersonUtil;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static no.fint.oneroster.util.StringNormalizer.normalize;

@Slf4j
@Repository
public class OneRosterRepository {
    private final OneRosterProperties oneRosterProperties;
    private final ClazzFactory clazzFactory;
    private final UserFactory userFactory;
    private final FintRepository fintRepository;

    private final ConcurrentMap<String, Base> cache = new ConcurrentSkipListMap<>();

    private final AtomicBoolean init = new AtomicBoolean(false);

    public OneRosterRepository(OneRosterProperties oneRosterProperties, ClazzFactory clazzFactory, UserFactory userFactory, FintRepository fintRepository) {
        this.oneRosterProperties = oneRosterProperties;
        this.clazzFactory = clazzFactory;
        this.userFactory = userFactory;
        this.fintRepository = fintRepository;
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

    public User getTeacherById(String sourcedId) {
        return getResourceByTypeAndId(User.class, sourcedId + "-t");
    }

    public User getStudentById(String sourcedId) {
        return getResourceByTypeAndId(User.class, sourcedId + "-s");
    }

    public List<AcademicSession> getAcademicSessions() {
        return getResourcesByType(AcademicSession.class);
    }

    public AcademicSession getAcademicSessionById(String sourcedId) {
        return getResourceByTypeAndId(AcademicSession.class, sourcedId);
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

    private BiConsumer<SkoleResource, Map<String, Base>> updateSchools(Org schoolOwner) {
        return (schoolResource, resources) -> {
            Org school = OrgFactory.school(schoolResource);

            if (schoolOwner.getChildren() == null) {
                schoolOwner.setChildren(new ArrayList<>());
            }

            school.setParent(GUIDRef.of(GUIDType.ORG, schoolOwner.getSourcedId()));

            schoolOwner.getChildren().add(GUIDRef.of(GUIDType.ORG, school.getSourcedId()));

            resources.put(school.getSourcedId(), school);

            updateStudents(schoolResource.getElevforhold())
                    .andThen(updateTeachers(schoolResource.getUndervisningsforhold()))
                    .andThen(updateStaffs(schoolResource.getUndervisningsforhold()))
                    .accept(resources);
        };
    }

    private Consumer<Map<String, Base>> updateStudents(List<Link> studentRelations) {
        return resources -> studentRelations.stream()
                .map(linkToString)
                .map(fintRepository::getStudentRelationById)
                .filter(Objects::nonNull)
                .forEach(studentRelation -> updateStudent(studentRelation).accept(resources));
    }

    private Consumer<Map<String, Base>> updateStudent(ElevforholdResource studentRelation) {
        return resources -> {
            Optional<ElevResource> student = getStudent(studentRelation);

            Optional<PersonResource> person = student
                    .map(ElevResource::getPerson)
                    .flatMap(this::getPerson);

            List<SkoleResource> schoolResources = getSchools(studentRelation.getSkole());

            if (student.isPresent() && person.isPresent() && !schoolResources.isEmpty()) {
                User user = userFactory.student(student.get(), person.get(), schoolResources);

                if (oneRosterProperties.isParents()) {
                    List<PersonResource> parents = person.get().getForeldre().stream()
                            .map(linkToString)
                            .map(fintRepository::getPersonById)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    parents.forEach(parent -> {
                        if (user.getAgents() == null) {
                            user.setAgents(new ArrayList<>());
                        }

                        user.getAgents().add(GUIDRef.of(GUIDType.USER, normalize(PersonUtil.maskNin(parent.getFodselsnummer().getIdentifikatorverdi()))));

                        updateParent(parent, student.get()).accept(resources);
                    });
                }

                String sid = user.getSourcedId() + "-s";

                resources.put(sid, user);
            }
        };
    }

    private Consumer<Map<String, Base>> updateParent(PersonResource parent, ElevResource child) {
        return resources -> {
            String sourcedId = normalize(PersonUtil.maskNin(parent.getFodselsnummer().getIdentifikatorverdi()));

            resources.computeIfPresent(sourcedId, (key, value) -> {
                User user = (User) value;

                user.getAgents().add(GUIDRef.of(GUIDType.USER, normalize(child.getSystemId().getIdentifikatorverdi())));

                return user;
            });

            resources.computeIfAbsent(sourcedId, it -> userFactory.parent(parent, child, oneRosterProperties.getOrg()));
        };
    }

    private Consumer<Map<String, Base>> updateTeachers(List<Link> teachingRelations) {
        return resources -> teachingRelations.stream()
                .map(linkToString)
                .map(fintRepository::getTeachingRelationById)
                .filter(Objects::nonNull)
                .filter(isTeacher)
                .forEach(teachingRelation -> updateTeacher(teachingRelation).accept(resources));
    }

    private Consumer<Map<String, Base>> updateStaffs(List<Link> teachingRelations) {
        return resources -> teachingRelations.stream()
                .map(linkToString)
                .map(fintRepository::getTeachingRelationById)
                .filter(Objects::nonNull)
                .filter(isStaff)
                .forEach(teachingRelation -> updateStaff(teachingRelation).accept(resources));
    }

    private Consumer<Map<String, Base>> updateStaff(UndervisningsforholdResource teachingRelation) {
        return resources -> {
            Optional<SkoleressursResource> staff = getTeacher(teachingRelation);

            Optional<PersonalressursResource> personnelResource = staff
                    .map(SkoleressursResource::getPersonalressurs)
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(linkToString)
                    .map(fintRepository::getPersonnelById)
                    .filter(Objects::nonNull)
                    .findAny();

            Optional<PersonResource> personResource = personnelResource
                    .map(PersonalressursResource::getPerson)
                    .flatMap(this::getPerson);

            List<SkoleResource> schoolResources = getSchools(teachingRelation.getSkole());

            if (staff.isPresent() && personnelResource.isPresent() && personResource.isPresent() && !schoolResources.isEmpty()) {
                User user = userFactory.administrator(staff.get(), personnelResource.get(), personResource.get(), schoolResources);

                String sid = user.getSourcedId() + "-a";

                resources.put(sid, user);
            }
        };
    }

    private Consumer<Map<String, Base>> updateTeacher(UndervisningsforholdResource teachingRelation) {
        return resources -> {
            Optional<SkoleressursResource> teacher = getTeacher(teachingRelation);

            Optional<PersonalressursResource> personnelResource = teacher
                    .map(SkoleressursResource::getPersonalressurs)
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .map(linkToString)
                    .map(fintRepository::getPersonnelById)
                    .filter(Objects::nonNull)
                    .findAny();

            Optional<PersonResource> personResource = personnelResource
                    .map(PersonalressursResource::getPerson)
                    .flatMap(this::getPerson);

            List<SkoleResource> schoolResources = getSchools(teachingRelation.getSkole());

            if (teacher.isPresent() && personnelResource.isPresent() && personResource.isPresent() && !schoolResources.isEmpty()) {
                User user = userFactory.teacher(teacher.get(), personnelResource.get(), personResource.get(), schoolResources);

                String sid = user.getSourcedId() + "-t";

                resources.put(sid, user);
            }
        };
    }

    private BiConsumer<SkoleResource, Map<String, Base>> updateBasisGroups() {
        return (schoolResource, resources) -> schoolResource.getBasisgruppe()
                .stream()
                .map(linkToString)
                .map(fintRepository::getBasisGroupById)
                .filter(Objects::nonNull)
                .forEach(basisGroup -> updateBasisGroup(basisGroup).accept(schoolResource, resources));
    }

    private BiConsumer<SkoleResource, Map<String, Base>> updateBasisGroup(BasisgruppeResource basisGroup) {
        return (schoolResource, resources) -> basisGroup.getTrinn()
                .stream()
                .map(linkToString)
                .map(fintRepository::getLevelById)
                .filter(Objects::nonNull)
                .findAny()
                .ifPresent(level -> {
                    List<TerminResource> terms = getTerms(basisGroup.getTermin());

                    Clazz clazz = clazzFactory.basisGroup(basisGroup, level, schoolResource, terms);

                    resources.put(clazz.getSourcedId(), clazz);

                    resources.computeIfAbsent(level.getSystemId().getIdentifikatorverdi(), it ->
                            CourseFactory.level(level, oneRosterProperties.getOrg()));

                    terms.forEach(term -> resources.computeIfAbsent(term.getSystemId().getIdentifikatorverdi(), it ->
                            AcademicSessionFactory.term(term, getSchoolYear(basisGroup.getSkolear()))));

                    addStudentEnrollment(basisGroup.getElevforhold(), schoolResource)
                            .andThen(addTeachingEnrollment(basisGroup.getUndervisningsforhold(), schoolResource))
                            .accept(basisGroup, resources);
                });
    }

    private BiConsumer<SkoleResource, Map<String, Base>> updateTeachingGroups() {
        return (schoolResource, resources) -> schoolResource.getUndervisningsgruppe()
                .stream()
                .map(linkToString)
                .map(fintRepository::getTeachingGroupById)
                .filter(Objects::nonNull)
                .forEach(teachingGroup -> updateTeachingGroup(teachingGroup).accept(schoolResource, resources));
    }

    private BiConsumer<SkoleResource, Map<String, Base>> updateTeachingGroup(UndervisningsgruppeResource teachingGroup) {
        return (schoolResource, resources) -> teachingGroup.getFag()
                .stream()
                .map(linkToString)
                .map(fintRepository::getSubjectById)
                .filter(Objects::nonNull)
                .findAny()
                .ifPresent(subject -> {
                    List<TerminResource> terms = getTerms(teachingGroup.getTermin());

                    Clazz clazz = clazzFactory.teachingGroup(teachingGroup, subject, schoolResource, terms);

                    resources.put(clazz.getSourcedId(), clazz);

                    resources.computeIfAbsent(subject.getSystemId().getIdentifikatorverdi(), it ->
                            CourseFactory.subject(subject, oneRosterProperties.getOrg()));

                    terms.forEach(term -> resources.computeIfAbsent(term.getSystemId().getIdentifikatorverdi(), it ->
                            AcademicSessionFactory.term(term, getSchoolYear(teachingGroup.getSkolear()))));

                    addStudentEnrollment(teachingGroup.getElevforhold(), schoolResource)
                            .andThen(addTeachingEnrollment(teachingGroup.getUndervisningsforhold(), schoolResource))
                            .accept(teachingGroup, resources);
                });
    }

    private BiConsumer<SkoleResource, Map<String, Base>> updateContactTeacherGroups() {
        return (schoolResource, resources) -> schoolResource.getKontaktlarergruppe()
                .stream()
                .map(linkToString)
                .map(fintRepository::getContactTeacherGroupById)
                .filter(Objects::nonNull)
                .forEach(contactTeacherGroup -> updateContactTeacherGroup(contactTeacherGroup).accept(schoolResource, resources));
    }

    private BiConsumer<SkoleResource, Map<String, Base>> updateContactTeacherGroup(KontaktlarergruppeResource contactTeacherGroup) {
        return (schoolResource, resources) -> contactTeacherGroup.getBasisgruppe()
                .stream()
                .findFirst()
                .map(linkToString)
                .map(fintRepository::getBasisGroupById)
                .map(BasisgruppeResource::getTrinn)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(linkToString)
                .map(fintRepository::getLevelById)
                .filter(Objects::nonNull)
                .findAny()
                .ifPresent(level -> {
                    List<TerminResource> terms = getTerms(contactTeacherGroup.getTermin());

                    Clazz clazz = clazzFactory.contactTeacherGroup(contactTeacherGroup, level, schoolResource, terms);

                    resources.put(clazz.getSourcedId(), clazz);

                    terms.forEach(term -> resources.computeIfAbsent(term.getSystemId().getIdentifikatorverdi(), it ->
                            AcademicSessionFactory.term(term, getSchoolYear(contactTeacherGroup.getSkolear()))));

                    addStudentEnrollment(contactTeacherGroup.getElevforhold(), schoolResource)
                            .andThen(addTeachingEnrollment(contactTeacherGroup.getUndervisningsforhold(), schoolResource))
                            .accept(contactTeacherGroup, resources);
                });
    }


    private BiConsumer<GruppeResource, Map<String, Base>> addStudentEnrollment(List<Link> studentRelations, SkoleResource school) {
        return (group, resources) -> studentRelations.stream()
                .map(linkToString)
                .map(fintRepository::getStudentRelationById)
                .filter(Objects::nonNull)
                .forEach(studentRelation -> getStudent(studentRelation).ifPresent(student -> {
                    Enrollment enrollment = EnrollmentFactory.student(studentRelation, student, group, school);

                    resources.put(enrollment.getSourcedId(), enrollment);
                }));
    }

    private BiConsumer<GruppeResource, Map<String, Base>> addTeachingEnrollment(List<Link> teachingRelations, SkoleResource school) {
        return (group, resources) -> teachingRelations.stream()
                .map(linkToString)
                .map(fintRepository::getTeachingRelationById)
                .filter(Objects::nonNull)
                .forEach(teachingRelation -> getTeacher(teachingRelation).ifPresent(teacher -> {
                    Enrollment enrollment = EnrollmentFactory.teacher(teachingRelation, teacher, group, school);

                    resources.put(enrollment.getSourcedId(), enrollment);
                }));
    }

    private List<TerminResource> getTerms(List<Link> terms) {
        return terms.stream()
                .map(linkToString)
                .map(fintRepository::getTermById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private SkolearResource getSchoolYear(List<Link> schoolYears) {
        return schoolYears.stream()
                .map(linkToString)
                .map(fintRepository::getSchoolYearById)
                .filter(Objects::nonNull)
                .findAny()
                .orElse(null);
    }

    private List<SkoleResource> getSchools(List<Link> schools) {
        return schools.stream()
                .map(linkToString)
                .map(fintRepository::getSchoolById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Optional<PersonResource> getPerson(List<Link> persons) {
        return persons.stream()
                .map(linkToString)
                .map(fintRepository::getPersonById)
                .filter(Objects::nonNull)
                .findAny();
    }

    private Optional<ElevResource> getStudent(ElevforholdResource studentRelation) {
        return studentRelation.getElev()
                .stream()
                .map(linkToString)
                .map(fintRepository::getStudentById)
                .filter(Objects::nonNull)
                .findAny();
    }

    private Optional<SkoleressursResource> getTeacher(UndervisningsforholdResource teachingRelation) {
        return teachingRelation.getSkoleressurs()
                .stream()
                .map(linkToString)
                .map(fintRepository::getTeacherById)
                .filter(Objects::nonNull)
                .findAny();
    }

    public void update() {
        Map<String, Base> resources = new HashMap<>();

        Org schoolOwner = OrgFactory.schoolOwner(oneRosterProperties.getOrg());

        fintRepository.getSchools().forEach(schoolResource -> {
            updateSchools(schoolOwner)
                    .andThen(updateBasisGroups())
                    .andThen(updateTeachingGroups())
                    .accept(schoolResource, resources);

            if (oneRosterProperties.isContactTeacherGroups()) {
                updateContactTeacherGroups().accept(schoolResource, resources);
            }
        });

        resources.put(schoolOwner.getSourcedId(), schoolOwner);

        updateCache(resources);
    }

    private void updateCache(Map<String, Base> resources) {
        MapDifference<String, Base> difference = Maps.difference(resources, cache);

        if (difference.areEqual()) {
            return;
        }

        difference.entriesOnlyOnLeft().entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().getClass().getSimpleName()))
                .forEach(entry -> {
                    if (init.get()) {
                        log.info("create {} - {}", entry.getValue().getClass().getSimpleName(), entry.getKey());
                    }
                    cache.put(entry.getKey(), entry.getValue());
                });

        difference.entriesOnlyOnRight().entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().getClass().getSimpleName()))
                .forEach(entry -> {
                    if (init.get()) {
                        log.info("delete {} - {}", entry.getValue().getClass().getSimpleName(), entry.getKey());
                    }

                    cache.remove(entry.getKey());
                });

        difference.entriesDiffering().entrySet()
                .stream()
                .sorted(Comparator.comparing(entry -> entry.getValue().getClass().getSimpleName()))
                .forEach(entry -> {
                    if (init.get()) {
                        log.info("update {} - {}", entry.getValue().rightValue().getClass().getSimpleName(), entry.getKey());
                    }
                    cache.put(entry.getKey(), entry.getValue().leftValue());
                });

        log.info("created: {}, deleted: {}, updated: {}", difference.entriesOnlyOnLeft().size(),
                difference.entriesOnlyOnRight().size(), difference.entriesDiffering().size());

        init.getAndSet(true);
    }

    private final Predicate<UndervisningsforholdResource> isTeacher = teachingRelation ->
            !teachingRelation.getBasisgruppe().isEmpty() ||
                    !teachingRelation.getUndervisningsgruppe().isEmpty() ||
                    !teachingRelation.getKontaktlarergruppe().isEmpty();

    private final Predicate<UndervisningsforholdResource> isStaff = teachingRelation ->
            teachingRelation.getBasisgruppe().isEmpty() &&
                    teachingRelation.getUndervisningsgruppe().isEmpty() &&
                    teachingRelation.getKontaktlarergruppe().isEmpty();

    private final Function<Link, String> linkToString = link -> Optional.ofNullable(link.getHref())
            .map(String::toLowerCase).orElse(null);
}