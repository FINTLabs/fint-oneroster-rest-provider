package no.fint.oneroster.factory;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.properties.OrganisationProperties;

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

    public static Org schoolOwner(OrganisationProperties.Organisation organisation) {
        Org schoolOwner = new Org(
                organisation.getSourcedId(),
                organisation.getName(),
                OrgType.DISTRICT
        );

        schoolOwner.setIdentifier(organisation.getIdentifier());

        return schoolOwner;
    }
}
