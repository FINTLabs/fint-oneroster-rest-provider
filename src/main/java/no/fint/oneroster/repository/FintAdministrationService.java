package no.fint.oneroster.repository;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.PersonResources;
import no.fint.oneroster.repository.FintRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FintAdministrationService {
    private final FintRepository fintRepository;

    private final Map<String, PersonalressursResource> personnel = new HashMap<>();
    private final Map<String, PersonResource> persons = new HashMap<>();

    public FintAdministrationService(FintRepository fintRepository) {
        this.fintRepository = fintRepository;
    }

    public Map<String, PersonResource> getPersons() {
        if (persons.isEmpty()) {
            updatePersons();
        }

        return persons;
    }

    public void updatePersons() {
        List<PersonResource> resources = fintRepository.getResources(PersonResources.class, "administration", "person")
                .toStream()
                .collect(Collectors.toList());

        if (resources.size() > 0) persons.clear();

        resources.forEach(resource -> this.getSelfLinks(resource).forEach(link -> persons.put(link, resource)));
    }

    public Map<String, PersonalressursResource> getPersonnel() {
        if (personnel.isEmpty()) {
            updatePersonnel();
        }

        return personnel;
    }

    public void updatePersonnel() {
        List<PersonalressursResource> resources = fintRepository.getResources(PersonalressursResources.class, "administration", "personnel")
                .toStream()
                .collect(Collectors.toList());

        if (resources.size() > 0) {
            personnel.clear();
        }

        resources.forEach(resource -> this.getSelfLinks(resource).forEach(link -> personnel.put(link, resource)));
    }

    private <T extends FintLinks> Stream<String> getSelfLinks(T resource) {
        return resource.getSelfLinks().stream()
                .map(Link::getHref)
                .map(String::toLowerCase);
    }
}