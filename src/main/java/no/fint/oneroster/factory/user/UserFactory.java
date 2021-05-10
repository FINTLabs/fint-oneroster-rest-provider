package no.fint.oneroster.factory.user;

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
import no.fint.oneroster.properties.OneRosterProperties;
import no.fint.oneroster.util.PersonUtil;

import java.util.*;
import java.util.stream.Collectors;

import static no.fint.oneroster.util.StringNormalizer.normalize;

public interface UserFactory {
    default User student(ElevResource elevResource, PersonResource personResource, List<SkoleResource> skoleResources) {
        User student = new User(
                normalize(elevResource.getSystemId().getIdentifikatorverdi()),
                Optional.ofNullable(elevResource.getBrukernavn()).map(Identifikator::getIdentifikatorverdi).orElse(""),
                true,
                personResource.getNavn().getFornavn(),
                personResource.getNavn().getEtternavn(),
                RoleType.STUDENT,
                skoleResources.stream()
                        .map(skoleResource -> GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())))
                        .collect(Collectors.toList())
        );

        Optional<ElevResource> resource = Optional.of(elevResource);

        resource.map(ElevResource::getKontaktinformasjon)
                .map(Kontaktinformasjon::getEpostadresse)
                .ifPresent(student::setEmail);

        resource.map(ElevResource::getFeidenavn)
                .map(Identifikator::getIdentifikatorverdi)
                .map(identifier -> new UserId("Feide", identifier))
                .ifPresent(userId -> student.setUserIds(Collections.singletonList(userId)));

        Optional.of(personResource.getNavn())
                .map(Personnavn::getMellomnavn)
                .ifPresent(student::setMiddleName);

        getNin(personResource).ifPresent(student::setIdentifier);

        return student;
    }

    default User teacher(SkoleressursResource skoleressursResource, PersonalressursResource personalressursResource, PersonResource personResource, List<SkoleResource> skoleResources) {
        User teacher = new User(
                normalize(skoleressursResource.getSystemId().getIdentifikatorverdi()),
                Optional.ofNullable(personalressursResource.getBrukernavn()).map(Identifikator::getIdentifikatorverdi).orElse(""),
                true,
                personResource.getNavn().getFornavn(),
                personResource.getNavn().getEtternavn(),
                RoleType.TEACHER,
                skoleResources.stream()
                        .map(skoleResource -> GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())))
                        .collect(Collectors.toList())
        );

        Optional<PersonalressursResource> resource = Optional.of(personalressursResource);

        resource.map(PersonalressursResource::getKontaktinformasjon)
                .map(Kontaktinformasjon::getEpostadresse)
                .ifPresent(teacher::setEmail);

        Optional.ofNullable(skoleressursResource.getFeidenavn())
                .map(Identifikator::getIdentifikatorverdi)
                .map(identifier -> new UserId("Feide", identifier))
                .ifPresent(userId -> teacher.setUserIds(Collections.singletonList(userId)));

        Optional.of(personResource.getNavn())
                .map(Personnavn::getMellomnavn)
                .ifPresent(teacher::setMiddleName);

        getNin(personResource).ifPresent(teacher::setIdentifier);

        return teacher;
    }

    default User parent(PersonResource personResource, ElevResource child, OneRosterProperties.Org org) {
        User parent = new User(
                normalize(PersonUtil.maskNin(personResource.getFodselsnummer().getIdentifikatorverdi())),
                "",
                true,
                personResource.getNavn().getFornavn(),
                personResource.getNavn().getEtternavn(),
                RoleType.PARENT,
                Collections.singletonList(GUIDRef.of(GUIDType.ORG, normalize(org.getSourcedId())))
        );

        parent.setAgents(new ArrayList<>(List.of(GUIDRef.of(GUIDType.USER, normalize(child.getSystemId().getIdentifikatorverdi())))));

        Optional<PersonResource> resource = Optional.of(personResource);

        resource.map(PersonResource::getKontaktinformasjon)
                .map(Kontaktinformasjon::getEpostadresse)
                .ifPresent(parent::setEmail);

        Optional.of(personResource.getNavn())
                .map(Personnavn::getMellomnavn)
                .ifPresent(parent::setMiddleName);

        getNin(personResource).ifPresent(parent::setIdentifier);

        return parent;
    }

    default Optional<String> getNin(PersonResource person) {
        return Optional.empty();
    }
}