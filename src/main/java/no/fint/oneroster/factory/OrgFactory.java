package no.fint.oneroster.factory;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.model.Org;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.properties.OneRosterProperties;

import java.util.Optional;

import static no.fint.oneroster.util.StringNormalizer.normalize;

public final class OrgFactory {

    private OrgFactory() {
    }

    public static Org school(SkoleResource skoleResource) {
        Org school = new Org(
                normalize(skoleResource.getSystemId().getIdentifikatorverdi()),
                skoleResource.getNavn(),
                OrgType.SCHOOL
        );

        Optional.ofNullable(skoleResource.getOrganisasjonsnummer())
                .map(Identifikator::getIdentifikatorverdi)
                .ifPresent(school::setIdentifier);

        return school;
    }

    public static Org schoolOwner(OneRosterProperties.Org org) {
        Org schoolOwner = new Org(
                normalize(org.getSourcedId()),
                org.getName(),
                OrgType.DISTRICT
        );

        schoolOwner.setIdentifier(org.getIdentifier());

        return schoolOwner;
    }
}
