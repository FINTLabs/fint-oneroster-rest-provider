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
import no.fint.oneroster.model.User;
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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        fintRepository.getStudents()
                .values()
                .forEach(student -> {
                    Optional<PersonResource> person = student.getPerson()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(fintRepository.getPersons()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    List<SkoleResource> schoolResources = student.getElevforhold()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(fintRepository.getStudentRelations()::get)
                            .filter(Objects::nonNull)
                            .map(ElevforholdResource::getSkole)
                            .flatMap(List::stream)
                            .map(LinkUtil::normalize)
                            .map(fintRepository.getSchools()::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (person.isPresent() && !schoolResources.isEmpty()) {
                        users.add(UserFactory.student(student, person.get(), schoolResources));
                    }
                });

        fintRepository.getTeachers()
                .values()
                .forEach(teacher -> {
                    Optional<PersonalressursResource> personnelResource = teacher.getPersonalressurs()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(fintRepository.getPersonnelResources()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    Optional<PersonResource> personResource = personnelResource
                            .map(PersonalressursResource::getPerson)
                            .orElseGet(Collections::emptyList)
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(fintRepository.getPersons()::get)
                            .filter(Objects::nonNull)
                            .findAny();

                    List<SkoleResource> schoolResources = teacher.getUndervisningsforhold()
                            .stream()
                            .map(LinkUtil::normalize)
                            .map(fintRepository.getTeachingRelations()::get)
                            .filter(Objects::nonNull)
                            .map(UndervisningsforholdResource::getSkole)
                            .flatMap(List::stream)
                            .map(LinkUtil::normalize)
                            .map(fintRepository.getSchools()::get)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (personnelResource.isPresent() && personResource.isPresent() && !schoolResources.isEmpty()) {
                        users.add(UserFactory.teacher(teacher, personnelResource.get(), personResource.get(), schoolResources));
                    }
                });

        return users;
    }

    public User getUser(String sourcedId) {
        return getAllUsers()
                .stream()
                .filter(user -> user.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getAllStudents() {
        return getAllUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.STUDENT))
                .collect(Collectors.toList());
    }

    public User getStudent(String sourcedId) {
        return getAllStudents()
                .stream()
                .filter(student -> student.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getStudentsForSchool(String sourcedId) {
        return getAllStudents()
                .stream()
                .filter(student -> student.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(sourcedId::equals))
                .collect(Collectors.toList());
    }

    public List<User> getAllTeachers() {
        return getAllUsers()
                .stream()
                .filter(user -> user.getRole().equals(RoleType.TEACHER))
                .collect(Collectors.toList());
    }

    public User getTeacher(String sourcedId) {
        return getAllTeachers()
                .stream()
                .filter(teacher -> teacher.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<User> getTeachersForSchool(String sourcedId) {
        return getAllTeachers()
                .stream()
                .filter(teacher -> teacher.getOrgs().stream().map(GUIDRef::getSourcedId).anyMatch(sourcedId::equals))
                .collect(Collectors.toList());
    }
}
