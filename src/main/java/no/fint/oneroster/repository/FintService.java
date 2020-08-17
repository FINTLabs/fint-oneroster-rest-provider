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

    private final Map<String, String> selfLinks = new HashMap<>();
    private final Map<String, FintLinks> resources = new HashMap<>();

    public FintService(FintRepository fintRepository) {
        this.fintRepository = fintRepository;
    }

    public List<SkoleResource> getSchools() {
        return getResourcesByType(SkoleResource.class);
    }

    public SkoleResource getSchoolById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (SkoleResource) resources.get(selfLinks);
    }

    public List<ElevResource> getStudents() {
        return getResourcesByType(ElevResource.class);
    }

    public ElevResource getStudentById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (ElevResource) resources.get(selfLinks);
    }

    public List<SkoleressursResource> getTeachers() {
        return getResourcesByType(SkoleressursResource.class);
    }

    public SkoleressursResource getTeacherById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (SkoleressursResource) resources.get(selfLinks);
    }

    public List<ElevforholdResource> getStudentRelations() {
        return getResourcesByType(ElevforholdResource.class);
    }

    public ElevforholdResource getStudentRelationById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (ElevforholdResource) resources.get(selfLinks);
    }

    public List<UndervisningsforholdResource> getTeachingRelations() {
        return getResourcesByType(UndervisningsforholdResource.class);
    }

    public UndervisningsforholdResource getTeachingRelationById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (UndervisningsforholdResource) resources.get(selfLinks);
    }

    public List<BasisgruppeResource> getBasisGroups() {
        return getResourcesByType(BasisgruppeResource.class);
    }

    public BasisgruppeResource getBasisGroupById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (BasisgruppeResource) resources.get(selfLinks);
    }

    public List<KontaktlarergruppeResource> getContactTeacherGroups() {
        return getResourcesByType(KontaktlarergruppeResource.class);
    }

    public KontaktlarergruppeResource getContactTeacherGroupById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (KontaktlarergruppeResource) resources.get(selfLinks);
    }

    public List<UndervisningsgruppeResource> getTeachingGroups() {
        return getResourcesByType(UndervisningsgruppeResource.class);
    }

    public UndervisningsgruppeResource getTeachingGroupById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (UndervisningsgruppeResource) resources.get(selfLinks);
    }

    public List<ArstrinnResource> getLevels() {
        return getResourcesByType(ArstrinnResource.class);
    }

    public ArstrinnResource getLevelById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (ArstrinnResource) resources.get(selfLinks);
    }

    public List<FagResource> getSubjects() {
        return getResourcesByType(FagResource.class);
    }

    public FagResource getSubjectById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (FagResource) resources.get(selfLinks);
    }

    public List<PersonResource> getPersons() {
        return getResourcesByType(PersonResource.class);
    }

    public PersonResource getPersonById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (PersonResource) resources.get(selfLinks);
    }

    public List<PersonalressursResource> getPersonnel() {
        return getResourcesByType(PersonalressursResource.class);
    }

    public PersonalressursResource getPersonnelById(String id) {
        String selfLinks = this.selfLinks.get(id);
        return (PersonalressursResource) resources.get(selfLinks);
    }

    public <T> List<T> getResourcesByType(Class<T> clazz) {
        return resources.values()
                .stream()
                .filter(clazz::isInstance)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    public void updateResources() {
        selfLinks.clear();
        resources.clear();

        Flux.merge(fintRepository.getEducationResources(SkoleResources.class, FintEndpoint.SCHOOL.getKey()),
                fintRepository.getEducationResources(PersonResources.class, FintEndpoint.PERSON.getKey()),
                fintRepository.getEducationResources(ElevResources.class, FintEndpoint.STUDENT.getKey()),
                fintRepository.getEducationResources(SkoleressursResources.class, FintEndpoint.TEACHER.getKey()),
                fintRepository.getEducationResources(ElevforholdResources.class, FintEndpoint.STUDENT_RELATION.getKey()),
                fintRepository.getEducationResources(UndervisningsforholdResources.class, FintEndpoint.TEACHING_RELATION.getKey()),
                fintRepository.getEducationResources(BasisgruppeResources.class, FintEndpoint.BASIS_GROUP.getKey()),
                fintRepository.getEducationResources(UndervisningsgruppeResources.class, FintEndpoint.TEACHING_GROUP.getKey()),
                fintRepository.getEducationResources(KontaktlarergruppeResources.class, FintEndpoint.CONTACT_TEACHER_GROUP.getKey()),
                fintRepository.getEducationResources(ArstrinnResources.class, FintEndpoint.LEVEL.getKey()),
                fintRepository.getEducationResources(FagResources.class, FintEndpoint.SUBJECT.getKey()),
                fintRepository.getAdministrationResources(PersonalressursResources.class, FintEndpoint.PERSONNEL.getKey()),
                fintRepository.getAdministrationResources(PersonResources.class, FintEndpoint.PERSON.getKey()))
                .toStream()
                .forEach(resource -> {
                    getSelfLinks(resource).forEach(link -> selfLinks.put(link, resource.getSelfLinks().toString()));
                    resources.put(resource.getSelfLinks().toString(), resource);
                });
    }

    private <T extends FintLinks> Stream<String> getSelfLinks(T resource) {
        return resource.getSelfLinks()
                .stream()
                .map(Link::getHref)
                .map(String::toLowerCase);
    }
}
