package no.fint.oneroster.repository;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.administrasjon.personal.PersonalressursResources;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.felles.PersonResources;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Slf4j
@Service
public class FintAdministrationService {
    private final FintRepository fintRepository;

    private final Map<String, PersonalressursResource> personnel = new ConcurrentHashMap<>();
    private final Map<String, PersonResource> persons = new ConcurrentHashMap<>();

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
        fintRepository.getResources(PersonResources.class, "administration", "person")
                .filter(resource -> Optional.ofNullable(resource.getNavn()).map(Personnavn::getFornavn).isPresent() &&
                        Optional.ofNullable(resource.getNavn()).map(Personnavn::getEtternavn).isPresent())
                .toStream()
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> persons.put(link, resource)));
    }

    public Map<String, PersonalressursResource> getPersonnel() {
        if (personnel.isEmpty()) {
            updatePersonnel();
        }

        return personnel;
    }

    public void updatePersonnel() {
        fintRepository.getResources(PersonalressursResources.class, "administration", "personnel")
                .filter(resource -> Optional.ofNullable(resource.getBrukernavn()).map(Identifikator::getIdentifikatorverdi).isPresent())
                .toStream()
                .forEach(resource -> this.getSelfLinks(resource).forEach(link -> personnel.put(link, resource)));
    }

    private <T extends FintLinks> Stream<String> getSelfLinks(T resource) {
        return resource.getSelfLinks().stream()
                .map(Link::getHref)
                .map(String::toLowerCase);
    }
}