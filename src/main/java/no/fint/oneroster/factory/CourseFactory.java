package no.fint.oneroster.factory;

import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.oneroster.model.Course;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.properties.OneRosterProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static no.fint.oneroster.util.StringNormalizer.normalize;

public final class CourseFactory {

    private CourseFactory() {
    }

    public static Course level(ArstrinnResource arstrinnResource, OneRosterProperties.Org org) {
        Course level = new Course(
                normalize(arstrinnResource.getSystemId().getIdentifikatorverdi()),
                arstrinnResource.getNavn(),
                GUIDRef.of(GUIDType.ORG, normalize(org.getSourcedId()))
        );

        arstrinnResource.getGrepreferanse()
                .stream()
                .map(Link::getHref)
                .findAny()
                .ifPresent(href -> level.setSubjectCodes(Collections.singletonList(href)));

        return level;
    }

    public static Course subject(FagResource fagResource, OneRosterProperties.Org org) {
        Course subject = new Course(
                normalize(fagResource.getSystemId().getIdentifikatorverdi()),
                fagResource.getNavn(),
                GUIDRef.of(GUIDType.ORG, normalize(org.getSourcedId()))
        );

        if (!fagResource.getVigoreferanse().isEmpty() || !fagResource.getGrepreferanse().isEmpty()) {
            subject.setSubjectCodes(new ArrayList<>());

            Stream.of(fagResource.getVigoreferanse(), fagResource.getGrepreferanse())
                    .flatMap(List::stream)
                    .map(Link::getHref)
                    .forEach(subject.getSubjectCodes()::add);
        }

        return subject;
    }
}
