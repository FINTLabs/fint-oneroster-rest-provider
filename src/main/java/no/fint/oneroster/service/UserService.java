package no.fint.oneroster.service;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.factory.UserFactory;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.model.User;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.model.vocab.RoleType;
import no.fint.oneroster.repository.FintRepository;
import no.fint.oneroster.util.LinkUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final FintRepository fintRepository;

    public UserService(FintRepository fintRepository) {
        this.fintRepository = fintRepository;
    }

    public List<User> getAllUsers(String orgId) {
        Map<String, SkoleResource> schools = fintRepository.getSchools(orgId);
        Map<String, PersonResource> persons = fintRepository.getPersons(orgId);

        List<User> users = new ArrayList<>();

        Map<String, ElevforholdResource> studentRelations = fintRepository.getStudentRelations(orgId);

        fintRepository.getStudents(orgId)
                .values()
                .forEach(student -> {
                    Optional<PersonResource> person = student.getPerson()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(persons::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    List<SkoleResource> skoleResources = student.getElevforhold()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(studentRelations::get)
                            .filter(Objects::nonNull)
                            .map(ElevforholdResource::getSkole)
                            .flatMap(List::stream)
                            .map(LinkUtil::normalize)
                            .map(schools::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (person.isPresent() && !skoleResources.isEmpty()) {
                        users.add(UserFactory.student(student, person.get(), skoleResources));
                    }
                });

        Map<String, PersonalressursResource> personnelResources = fintRepository.getPersonnelResources(orgId);
        Map<String, UndervisningsforholdResource> teachingRelations = fintRepository.getTeachingRelations(orgId);

        fintRepository.getTeachers(orgId)
                .values()
                .forEach(teacher -> {
                    Optional<PersonalressursResource> personnelResource = teacher.getPersonalressurs()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(personnelResources::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    Optional<PersonResource> personResource = personnelResource
                            .map(PersonalressursResource::getPerson)
                            .orElseGet(Collections::emptyList)
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(persons::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    List<SkoleResource> schoolResources = teacher.getUndervisningsforhold()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(teachingRelations::get)
                            .filter(Objects::nonNull)
                            .map(UndervisningsforholdResource::getSkole)
                            .flatMap(List::stream)
                            .map(LinkUtil::normalize)
                            .map(schools::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (personnelResource.isPresent() && personResource.isPresent() && !schools.isEmpty()) {
                        users.add(UserFactory.teacher(teacher, personnelResource.get(), personResource.get(), schoolResources));
                    }
                });

        return users;
    }

    public User getUser(String orgId, String sourcedId) {
        return getAllUsers(orgId)
                .stream()
                .filter(user -> user.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getAllStudents(String orgId) {
        return getAllUsers(orgId)
                .stream()
                .filter(user -> user.getRole().equals(RoleType.STUDENT))
                .collect(Collectors.toList());
    }

    public User getStudent(String orgId, String sourcedId) {
        return getAllStudents(orgId)
                .stream()
                .filter(student -> student.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getStudentsForSchool(String orgId, String sourcedId) {
        return getAllStudents(orgId)
                .stream()
                .filter(student -> student.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(sourcedId::equals))
                .collect(Collectors.toList());
    }

    public List<User> getAllTeachers(String orgId) {
        return getAllUsers(orgId)
                .stream()
                .filter(user -> user.getRole().equals(RoleType.TEACHER))
                .collect(Collectors.toList());
    }

    public User getTeacher(String orgId, String sourcedId) {
        return getAllTeachers(orgId).stream()
                .filter(teacher -> teacher.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getTeachersForSchool(String orgId, String sourcedId) {
        return getAllTeachers(orgId)
                .stream()
                .filter(teacher -> teacher.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(sourcedId::equals))
                .collect(Collectors.toList());
    }
}
