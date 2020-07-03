package no.fint.oneroster.repository;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FintService {
    private final FintRepository fintRepository;

    private final Map<String, Object> resources = new HashMap<>();

    public FintService(FintRepository fintRepository) {
        this.fintRepository = fintRepository;
    }

    public List<SkoleResource> getSchools() {
        return getResourcesByType(SkoleResource.class);
    }

    public SkoleResource getSchoolById(String id) {
        return (SkoleResource) resources.get(id);
    }

    public List<ElevResource> getStudents() {
        return getResourcesByType(ElevResource.class);
    }

    public ElevResource getStudentById(String id) {
        return (ElevResource) resources.get(id);
    }

    public List<SkoleressursResource> getTeachers() {
        return getResourcesByType(SkoleressursResource.class);
    }

    public SkoleressursResource getTeacherById(String id) {
        return (SkoleressursResource) resources.get(id);
    }

    public List<ElevforholdResource> getStudentRelations() {
        return getResourcesByType(ElevforholdResource.class);
    }

    public ElevforholdResource getStudentRelationById(String id) {
        return (ElevforholdResource) resources.get(id);
    }

    public List<UndervisningsforholdResource> getTeachingRelations() {
        return getResourcesByType(UndervisningsforholdResource.class);
    }

    public UndervisningsforholdResource getTeachingRelationById(String id) {
        return (UndervisningsforholdResource) resources.get(id);
    }

    public List<BasisgruppeResource> getBasisGroups() {
        return getResourcesByType(BasisgruppeResource.class);
    }

    public BasisgruppeResource getBasisGroupById(String id) {
        return (BasisgruppeResource) resources.get(id);
    }

    public List<UndervisningsgruppeResource> getTeachingGroups() {
        return getResourcesByType(UndervisningsgruppeResource.class);
    }

    public UndervisningsgruppeResource getTeachingGroupById(String id) {
        return (UndervisningsgruppeResource) resources.get(id);
    }

    public List<ArstrinnResource> getLevels() {
        return getResourcesByType(ArstrinnResource.class);
    }

    public ArstrinnResource getLevelById(String id) {
        return (ArstrinnResource) resources.get(id);
    }

    public List<FagResource> getSubjects() {
        return getResourcesByType(FagResource.class);
    }

    public FagResource getSubjectById(String id) {
        return (FagResource) resources.get(id);
    }

    public List<PersonResource> getPersons() {
        return getResourcesByType(PersonResource.class);
    }

    public PersonResource getPersonById(String id) {
        return (PersonResource) resources.get(id);
    }

    public PersonalressursResource getPersonnelById(String id) {
        return (PersonalressursResource) resources.get(id);
    }

    public <T> List<T> getResourcesByType(Class<T> clazz) {
        return resources.values()
                .stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .distinct()
                .collect(Collectors.toList());
    }

    public void updateResources() {
        resources.clear();

        Flux.merge(fintRepository.getEducationResources(SkoleResources.class, FintEndpoint.SCHOOL.getKey()),
                fintRepository.getEducationResources(PersonResources.class, FintEndpoint.PERSON.getKey()),
                fintRepository.getEducationResources(ElevResources.class, FintEndpoint.STUDENT.getKey()),
                fintRepository.getEducationResources(SkoleressursResources.class, FintEndpoint.TEACHER.getKey()),
                fintRepository.getEducationResources(ElevforholdResources.class, FintEndpoint.STUDENT_RELATION.getKey()),
                fintRepository.getEducationResources(UndervisningsforholdResources.class, FintEndpoint.TEACHING_RELATION.getKey()),
                fintRepository.getEducationResources(BasisgruppeResources.class, FintEndpoint.BASIS_GROUP.getKey()),
                fintRepository.getEducationResources(UndervisningsgruppeResources.class, FintEndpoint.TEACHING_GROUP.getKey()),
                fintRepository.getEducationResources(ArstrinnResources.class, FintEndpoint.LEVEL.getKey()),
                fintRepository.getEducationResources(FagResources.class, FintEndpoint.SUBJECT.getKey()),
                fintRepository.getAdministrationResources(PersonalressursResources.class, FintEndpoint.PERSONNEL.getKey()),
                fintRepository.getAdministrationResources(PersonResources.class, FintEndpoint.PERSON.getKey()))
                .toStream()
                .forEach(resource -> getSelfLinks(resource).forEach(link -> resources.put(link, resource)));
    }

    private <T extends FintLinks> Stream<String> getSelfLinks(T resource) {
        return resource.getSelfLinks()
                .stream()
                .map(Link::getHref)
                .map(String::toLowerCase);
    }
}
