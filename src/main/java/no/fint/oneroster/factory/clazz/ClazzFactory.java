package no.fint.oneroster.factory.clazz;

import no.fint.model.resource.Link;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.utdanning.basisklasser.Gruppe;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.vocab.ClazzType;
import no.fint.oneroster.model.vocab.GUIDType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static no.fint.oneroster.util.StringNormalizer.normalize;

public interface ClazzFactory {
    default Clazz basisGroup(BasisgruppeResource basisgruppeResource, ArstrinnResource arstrinnResource, SkoleResource skoleResource, List<AcademicSession> terms) {
        Clazz basisGroup = new Clazz(
                normalize(basisgruppeResource.getSystemId().getIdentifikatorverdi()),
                basisGroupNameConverter(basisgruppeResource),
                ClazzType.HOMEROOM,
                GUIDRef.of(GUIDType.COURSE, normalize(arstrinnResource.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())),
                terms.stream()
                        .map(term -> GUIDRef.of(GUIDType.ACADEMICSESSION, term.getSourcedId()))
                        .collect(Collectors.toList()));

        arstrinnResource.getGrepreferanse()
                .stream()
                .map(Link::getHref)
                .findAny()
                .ifPresent(href -> basisGroup.setSubjectCodes(Collections.singletonList(href)));

        return basisGroup;
    }

    default Clazz teachingGroup(UndervisningsgruppeResource undervisningsgruppeResource, FagResource fagResource, SkoleResource skoleResource, List<AcademicSession> terms) {
        Clazz teachingGroup = new Clazz(
                normalize(undervisningsgruppeResource.getSystemId().getIdentifikatorverdi()),
                teachingGroupNameConverter(undervisningsgruppeResource),
                ClazzType.SCHEDULED,
                GUIDRef.of(GUIDType.COURSE, normalize(fagResource.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())),
                terms.stream()
                        .map(term -> GUIDRef.of(GUIDType.ACADEMICSESSION, term.getSourcedId()))
                        .collect(Collectors.toList()));

        if (!fagResource.getVigoreferanse().isEmpty() || !fagResource.getGrepreferanse().isEmpty()) {
            teachingGroup.setSubjectCodes(new ArrayList<>());

            Stream.of(fagResource.getVigoreferanse(), fagResource.getGrepreferanse())
                    .flatMap(List::stream)
                    .map(Link::getHref)
                    .forEach(teachingGroup.getSubjectCodes()::add);
        }

        return teachingGroup;
    }

    default String basisGroupNameConverter(Gruppe basisGroup) {
        return basisGroup.getNavn();
    }

    default String teachingGroupNameConverter(Gruppe teachingGroup) {
        return teachingGroup.getNavn();
    }
}