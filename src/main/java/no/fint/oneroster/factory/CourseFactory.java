package no.fint.oneroster.factory;

import no.fint.model.resource.Link;
import no.fint.model.resource.administrasjon.organisasjon.OrganisasjonselementResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.vocab.GUIDType;

import java.util.Collections;
import java.util.Optional;

public final class CourseFactory {

    private CourseFactory() {
    }

    public static Course level(ArstrinnResource arstrinnResource, OrganisasjonselementResource organisasjonselementResource) {
        Course level = new Course(
                arstrinnResource.getSystemId().getIdentifikatorverdi(),
                arstrinnResource.getNavn(),
                GUIDRef.of(GUIDType.ORG, organisasjonselementResource.getOrganisasjonsId().getIdentifikatorverdi())
        );

        Optional.ofNullable(arstrinnResource.getGrepreferanse())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Link::getHref)
                .findAny()
                .ifPresent(level::setCourseCode);

        return level;
    }

    public static Course subject(FagResource fagResource, OrganisasjonselementResource organisasjonselementResource) {
        Course subject = new Course(
                fagResource.getSystemId().getIdentifikatorverdi(),
                fagResource.getNavn(),
                GUIDRef.of(GUIDType.ORG, organisasjonselementResource.getOrganisasjonsId().getIdentifikatorverdi())
        );

        Optional.ofNullable(fagResource.getGrepreferanse())
                .orElseGet(Collections::emptyList)
                .stream()
                .map(Link::getHref)
                .findAny()
                .ifPresent(subject::setCourseCode);

        return subject;
    }
}
