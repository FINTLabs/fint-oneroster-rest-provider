package no.fint.oneroster.factory;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.felles.kompleksedatatyper.Kontaktinformasjon;
import no.fint.model.felles.kompleksedatatyper.Personnavn;
import no.fint.model.resource.administrasjon.personal.PersonalressursResource;
import no.fint.model.resource.felles.PersonResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.User;
import no.fint.oneroster.model.UserId;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.model.vocab.RoleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static no.fint.oneroster.util.StringNormalizer.normalize;

public final class UserFactory {

    private UserFactory() {
    }

    public static User student(ElevResource elevResource, PersonResource personResource, List<SkoleResource> skoleResources) {
        User student = new User(
                normalize(elevResource.getSystemId().getIdentifikatorverdi()),
                elevResource.getBrukernavn().getIdentifikatorverdi(),
                true,
                personResource.getNavn().getFornavn(),
                personResource.getNavn().getEtternavn(),
                RoleType.STUDENT,
                skoleResources.stream()
                        .map(skoleResource -> GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())))
                        .collect(Collectors.toList())
        );

        Optional<ElevResource> resource = Optional.of(elevResource);

        resource.map(ElevResource::getElevnummer)
                .map(Identifikator::getIdentifikatorverdi)
                .ifPresent(student::setIdentifier);
        resource.map(ElevResource::getKontaktinformasjon)
                .map(Kontaktinformasjon::getEpostadresse)
                .ifPresent(student::setEmail);
        resource.map(ElevResource::getKontaktinformasjon)
                .map(Kontaktinformasjon::getMobiltelefonnummer)
                .ifPresent(student::setSms);
        resource.map(ElevResource::getKontaktinformasjon)
                .map(Kontaktinformasjon::getTelefonnummer)
                .ifPresent(student::setPhone);

        List<UserId> userIds = new ArrayList<>();
        resource.map(ElevResource::getFeidenavn)
                .map(Identifikator::getIdentifikatorverdi)
                .map(identifier -> new UserId("Feide", identifier))
                .ifPresent(userIds::add);

        if (!userIds.isEmpty()) {
            student.setUserIds(userIds);
        }

        Optional.ofNullable(personResource.getNavn())
                .map(Personnavn::getMellomnavn)
                .ifPresent(student::setMiddleName);

        return student;
    }

    public static User teacher(SkoleressursResource skoleressursResource, PersonalressursResource personalressursResource, PersonResource personResource, List<SkoleResource> skoleResources) {
        User teacher = new User(
                normalize(skoleressursResource.getSystemId().getIdentifikatorverdi()),
                personalressursResource.getBrukernavn().getIdentifikatorverdi(),
                true,
                personResource.getNavn().getFornavn(),
                personResource.getNavn().getEtternavn(),
                RoleType.TEACHER,
                skoleResources.stream()
                        .map(skoleResource -> GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())))
                        .collect(Collectors.toList())
        );

        Optional<PersonalressursResource> resource = Optional.of(personalressursResource);

        resource.map(PersonalressursResource::getAnsattnummer)
                .map(Identifikator::getIdentifikatorverdi)
                .ifPresent(teacher::setIdentifier);
        resource.map(PersonalressursResource::getKontaktinformasjon)
                .map(Kontaktinformasjon::getEpostadresse)
                .ifPresent(teacher::setEmail);
        resource.map(PersonalressursResource::getKontaktinformasjon)
                .map(Kontaktinformasjon::getMobiltelefonnummer)
                .ifPresent(teacher::setSms);
        resource.map(PersonalressursResource::getKontaktinformasjon)
                .map(Kontaktinformasjon::getTelefonnummer)
                .ifPresent(teacher::setPhone);

        List<UserId> userIds = new ArrayList<>();
        Optional.ofNullable(skoleressursResource.getFeidenavn())
                .map(Identifikator::getIdentifikatorverdi)
                .map(identifier -> new UserId("Feide", identifier))
                .ifPresent(userIds::add);
        if (!userIds.isEmpty()) {
            teacher.setUserIds(userIds);
        }

        Optional.ofNullable(personResource.getNavn())
                .map(Personnavn::getMellomnavn)
                .ifPresent(teacher::setMiddleName);

        return teacher;
    }
}
