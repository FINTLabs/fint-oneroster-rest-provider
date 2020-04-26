package no.fint.oneroster.factory.clazz;

import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.ArstrinnResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.Clazz;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.vocab.ClazzType;
import no.fint.oneroster.model.vocab.GUIDType;

import java.util.List;
import java.util.stream.Collectors;

public interface ClazzFactory {
    default Clazz basisGroup(BasisgruppeResource basisgruppeResource, ArstrinnResource arstrinnResource, SkoleResource skoleResource, List<AcademicSession> terms) {
        return new Clazz(
                basisgruppeResource.getSystemId().getIdentifikatorverdi(),
                basisGroupNameConverter(basisgruppeResource.getNavn()),
                ClazzType.HOMEROOM,
                GUIDRef.of(GUIDType.COURSE, arstrinnResource.getSystemId().getIdentifikatorverdi()),
                GUIDRef.of(GUIDType.ORG, skoleResource.getSystemId().getIdentifikatorverdi()),
                terms.stream()
                        .map(term -> GUIDRef.of(GUIDType.ACADEMICSESSION, term.getSourcedId()))
                        .collect(Collectors.toList())
        );
    }

    default Clazz teachingGroup(UndervisningsgruppeResource undervisningsgruppeResource, FagResource fagResource, SkoleResource skoleResource, List<AcademicSession> terms) {
        return new Clazz(
                undervisningsgruppeResource.getSystemId().getIdentifikatorverdi(),
                teachingGroupNameConverter(undervisningsgruppeResource.getNavn()),
                ClazzType.SCHEDULED,
                GUIDRef.of(GUIDType.COURSE, fagResource.getSystemId().getIdentifikatorverdi()),
                GUIDRef.of(GUIDType.ORG, skoleResource.getSystemId().getIdentifikatorverdi()),
                terms.stream()
                        .map(term -> GUIDRef.of(GUIDType.ACADEMICSESSION, term.getSourcedId()))
                        .collect(Collectors.toList())
        );
    }

    default String basisGroupNameConverter(String name) {
        return name;
    }

    default String teachingGroupNameConverter(String name) {
        return name;
    }
}
