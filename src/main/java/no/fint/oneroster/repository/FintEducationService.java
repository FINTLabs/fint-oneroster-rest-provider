package no.fint.oneroster.repository;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.PersonResources;
import no.fint.model.resource.utdanning.elev.*;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.FagResources;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResources;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResources;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResources;
import no.fint.oneroster.properties.OneRosterProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
public class FintEducationService {
    private final FintRepository fintRepository;
    private final OneRosterProperties oneRosterProperties;

    private final Map<String, SkoleResource> schools = new HashMap<>();
    private final Map<String, PersonResource> persons = new HashMap<>();
    private final Map<String, ElevResource> students = new HashMap<>();
    private final Map<String, SkoleressursResource> teachers = new HashMap<>();
    private final Map<String, ElevforholdResource> studentRelations = new HashMap<>();
    private final Map<String, UndervisningsforholdResource> teachingRelations = new HashMap<>();
    private final Map<String, BasisgruppeResource> basisGroups = new HashMap<>();
    private final Map<String, UndervisningsgruppeResource> teachingGroups = new HashMap<>();
    private final Map<String, ArstrinnResource> levels = new HashMap<>();
    private final Map<String, FagResource> subjects = new HashMap<>();

    public FintEducationService(FintRepository fintRepository, OneRosterProperties oneRosterProperties) {
        this.fintRepository = fintRepository;
        this.oneRosterProperties = oneRosterProperties;
    }

    public Map<String, SkoleResource> getSchools() {
        if (schools.isEmpty()) {
            updateSchools();
        }

        return schools;
    }

    public void updateSchools() {
        fintRepository.getResources(SkoleResources.class, "education", "school")
                .toStream()
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> schools.put(link, resource)));
    }

    public Map<String, PersonResource> getPersons() {
        if (persons.isEmpty()) {
            updatePersons();
        }

        return persons;
    }

    public void updatePersons() {
        fintRepository.getResources(PersonResources.class, "education", "person")
                .toStream()
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> persons.put(link, resource)));
    }

    public Map<String, ElevResource> getStudents() {
        if (students.isEmpty()) {
            updateStudents();
        }

        return students;
    }

    public void updateStudents() {
        fintRepository.getResources(ElevResources.class, "education", "student")
                .toStream()
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> students.put(link, resource)));
    }

    public Map<String, SkoleressursResource> getTeachers() {
        if (teachers.isEmpty()) {
            updateTeachers();
        }

        return teachers;
    }

    public void updateTeachers() {
        fintRepository.getResources(SkoleressursResources.class, "education", "teacher")
                .toStream()
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> teachers.put(link, resource)));
    }

    public Map<String, ElevforholdResource> getStudentRelations() {
        if (studentRelations.isEmpty()) {
            updateStudentRelations();
        }

        return studentRelations;
    }

    public void updateStudentRelations() {
        fintRepository.getResources(ElevforholdResources.class, "education", "student-relation")
                .toStream()
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> studentRelations.put(link, resource)));
    }

    public Map<String, UndervisningsforholdResource> getTeachingRelations() {
        if (teachingRelations.isEmpty()) {
            updateTeachingRelations();
        }

        return teachingRelations;
    }

    public void updateTeachingRelations() {
        fintRepository.getResources(UndervisningsforholdResources.class, "education", "teaching-relation")
                .toStream()
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> teachingRelations.put(link, resource)));
    }

    public Map<String, BasisgruppeResource> getBasisGroups() {
        if (basisGroups.isEmpty()) {
            updateBasisGroups();
        }

        return basisGroups;
    }

    public void updateBasisGroups() {
        fintRepository.getResources(BasisgruppeResources.class, "education", "basis-group")
                .toStream()
                .filter(resource -> oneRosterProperties.getProfile().getClazzFilter()
                        .stream()
                        .noneMatch(filter -> resource.getNavn().concat(resource.getBeskrivelse()).contains(filter)))
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> basisGroups.put(link, resource)));
    }

    public Map<String, UndervisningsgruppeResource> getTeachingGroups() {
        if (teachingGroups.isEmpty()) {
            updateTeachingGroups();
        }

        return teachingGroups;
    }

    public void updateTeachingGroups() {
        fintRepository.getResources(UndervisningsgruppeResources.class, "education", "teaching-group")
                .toStream()
                .filter(resource -> oneRosterProperties.getProfile().getClazzFilter()
                        .stream()
                        .noneMatch(filter -> resource.getNavn().concat(resource.getBeskrivelse()).contains(filter)))
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> teachingGroups.put(link, resource)));
    }

    public Map<String, ArstrinnResource> getLevels() {
        if (levels.isEmpty()) {
            updateLevels();
        }

        return levels;
    }

    public void updateLevels() {
        fintRepository.getResources(ArstrinnResources.class, "education", "level")
                .toStream()
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> levels.put(link, resource)));
    }

    public Map<String, FagResource> getSubjects() {
        if (subjects.isEmpty()) {
            updateSubjects();
        }

        return subjects;
    }

    public void updateSubjects() {
        fintRepository.getResources(FagResources.class, "education", "subject")
                .toStream()
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> subjects.put(link, resource)));
    }

    private <T extends FintLinks> Stream<String> getSelfLinks(T resource) {
        return resource.getSelfLinks().stream()
                .map(Link::getHref)
                .map(String::toLowerCase);
    }
}