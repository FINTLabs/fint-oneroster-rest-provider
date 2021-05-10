package no.fint.oneroster.factory.user;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.felles.PersonResource;

import java.util.Optional;

public class NinUserFactory implements UserFactory {

    @Override
    public Optional<String> getNin(PersonResource person) {
        return Optional.of(person)
                .map(PersonResource::getFodselsnummer)
                .map(Identifikator::getIdentifikatorverdi);
    }
}
