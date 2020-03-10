package no.fint.oneroster.factory;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.model.vocab.OrgType;

import java.util.Optional;

public final class OrgFactory {

    private OrgFactory() {
    }

    public static Org school(SkoleResource skoleResource) {
        Org school = new Org(
                skoleResource.getSystemId().getIdentifikatorverdi(),
                skoleResource.getNavn(),
                OrgType.SCHOOL
        );

        Optional.ofNullable(skoleResource.getOrganisasjonsnummer())
                .map(Identifikator::getIdentifikatorverdi)
                .ifPresent(school::setIdentifier);

        return school;
    }

    public static Org schoolOwner(OrganisasjonselementResource organisasjonselementResource) {
        Org schoolOwner = new Org(
                organisasjonselementResource.getOrganisasjonsId().getIdentifikatorverdi(),
                organisasjonselementResource.getNavn(),
                OrgType.DISTRICT
        );

        Optional.ofNullable(organisasjonselementResource.getOrganisasjonsnummer())
                .map(Identifikator::getIdentifikatorverdi)
                .ifPresent(schoolOwner::setIdentifier);

        return schoolOwner;
    }
}
