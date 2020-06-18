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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<SkoleResource> resources = fintRepository.getResources(SkoleResources.class, "education", "school")
                .toStream()
                .collect(Collectors.toList());

        if (resources.size() > 0) {
            schools.clear();
        }

        resources.forEach(resource -> this.getSelfLinks(resource).forEach(link -> schools.put(link, resource)));
    }

    public Map<String, PersonResource> getPersons() {
        if (persons.isEmpty()) {
            updatePersons();
        }

        return persons;
    }

    public void updatePersons() {
        List<PersonResource> resources = fintRepository.getResources(PersonResources.class, "education", "person")
                .collectList()
                .blockOptional()
                .orElseGet(Collections::emptyList);

        if (resources.size() > 0) persons.clear();

        resources.forEach(resource -> this.getSelfLinks(resource).forEach(link -> persons.put(link, resource)));
    }

    public Map<String, ElevResource> getStudents() {
        if (students.isEmpty()) {
            updateStudents();
        }

        return students;
    }

    public void updateStudents() {
        List<ElevResource> resources = fintRepository.getResources(ElevResources.class, "education", "student")
                .collectList()
                .blockOptional()
                .orElseGet(Collections::emptyList);

        if (resources.size() > 0) students.clear();

        resources.forEach(resource -> this.getSelfLinks(resource).forEach(link -> students.put(link, resource)));
    }

    public Map<String, SkoleressursResource> getTeachers() {
        if (teachers.isEmpty()) {
            updateTeachers();
        }

        return teachers;
    }

    public void updateTeachers() {
        List<SkoleressursResource> resources = fintRepository.getResources(SkoleressursResources.class, "education", "teacher")
                .collectList()
                .blockOptional()
                .orElseGet(Collections::emptyList);

        if (resources.size() > 0) teachers.clear();

        resources.forEach(resource -> this.getSelfLinks(resource).forEach(link -> teachers.put(link, resource)));
    }

    public Map<String, ElevforholdResource> getStudentRelations() {
        if (studentRelations.isEmpty()) {
            updateStudentRelations();
        }

        return studentRelations;
    }

    public void updateStudentRelations() {
        List<ElevforholdResource> resources = fintRepository.getResources(ElevforholdResources.class, "education", "student-relation")
                .collectList()
                .blockOptional()
                .orElseGet(Collections::emptyList);

        if (resources.size() > 0) studentRelations.clear();

        resources.forEach(resource -> this.getSelfLinks(resource).forEach(link -> studentRelations.put(link, resource)));
    }

    public Map<String, UndervisningsforholdResource> getTeachingRelations() {
        if (teachingRelations.isEmpty()) {
            updateTeachingRelations();
        }

        return teachingRelations;
    }

    public void updateTeachingRelations() {
        List<UndervisningsforholdResource> resources = fintRepository.getResources(UndervisningsforholdResources.class, "education", "teaching-relation")
                .collectList()
                .blockOptional()
                .orElseGet(Collections::emptyList);

        if (resources.size() > 0) teachingRelations.clear();

        resources.forEach(resource -> this.getSelfLinks(resource).forEach(link -> teachingRelations.put(link, resource)));
    }

    public Map<String, BasisgruppeResource> getBasisGroups() {
        if (basisGroups.isEmpty()) {
            updateBasisGroups();
        }

        return basisGroups;
    }

    public void updateBasisGroups() {
        List<BasisgruppeResource> resources = fintRepository.getResources(BasisgruppeResources.class, "education", "basis-group")
                .collectList()
                .blockOptional()
                .orElseGet(Collections::emptyList);

        if (resources.size() > 0) basisGroups.clear();

        resources.stream()
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
        List<UndervisningsgruppeResource> resources = fintRepository.getResources(UndervisningsgruppeResources.class, "education", "teaching-group")
                .collectList()
                .blockOptional()
                .orElseGet(Collections::emptyList);

        if (resources.size() > 0) teachingGroups.clear();

        resources.stream()
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
        List<ArstrinnResource> resources = fintRepository.getResources(ArstrinnResources.class, "education", "level")
                .collectList()
                .blockOptional()
                .orElseGet(Collections::emptyList);

        if (resources.size() > 0) levels.clear();

        resources.forEach(resource -> this.getSelfLinks(resource).forEach(link -> levels.put(link, resource)));
    }

    public Map<String, FagResource> getSubjects() {
        if (subjects.isEmpty()) {
            updateSubjects();
        }

        return subjects;
    }

    public void updateSubjects() {
        List<FagResource> resources = fintRepository.getResources(FagResources.class, "education", "subject")
                .collectList()
                .blockOptional()
                .orElseGet(Collections::emptyList);

        if (resources.size() > 0) subjects.clear();

        resources.forEach(resource -> this.getSelfLinks(resource).forEach(link -> subjects.put(link, resource)));
    }

    private <T extends FintLinks> Stream<String> getSelfLinks(T resource) {
        return resource.getSelfLinks().stream()
                .map(Link::getHref)
                .map(String::toLowerCase);
    }
}