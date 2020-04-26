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
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class VTFKClazzFactory implements ClazzFactory {
    private final String PREFIX = "OF";
    private final String SUFFIX = "Klasse";
    private final String SEPARATOR = "-";

    public Clazz basisGroup(BasisgruppeResource basisgruppeResource, ArstrinnResource arstrinnResource, SkoleResource skoleResource, List<AcademicSession> terms) {
        return new Clazz(
                basisgruppeResource.getSystemId().getIdentifikatorverdi(),
                basisGroupNameConverter(basisgruppeResource.getSystemId().getIdentifikatorverdi()),
                ClazzType.HOMEROOM,
                GUIDRef.of(GUIDType.COURSE, arstrinnResource.getSystemId().getIdentifikatorverdi()),
                GUIDRef.of(GUIDType.ORG, skoleResource.getSystemId().getIdentifikatorverdi()),
                terms.stream()
                        .map(term -> GUIDRef.of(GUIDType.ACADEMICSESSION, term.getSourcedId()))
                        .collect(Collectors.toList())
        );
    }

    public Clazz teachingGroup(UndervisningsgruppeResource undervisningsgruppeResource, FagResource fagResource, SkoleResource skoleResource, List<AcademicSession> terms) {
        return new Clazz(
                undervisningsgruppeResource.getSystemId().getIdentifikatorverdi(),
                undervisningsgruppeResource.getNavn(),
                ClazzType.SCHEDULED,
                GUIDRef.of(GUIDType.COURSE, fagResource.getSystemId().getIdentifikatorverdi()),
                GUIDRef.of(GUIDType.ORG, skoleResource.getSystemId().getIdentifikatorverdi()),
                terms.stream()
                        .map(term -> GUIDRef.of(GUIDType.ACADEMICSESSION, term.getSourcedId()))
                        .collect(Collectors.toList())
        );
    }

    public String basisGroupNameConverter(String name) {
        String between = StringUtils.substringBetween(name,"_", "@");
        return PREFIX +
                SEPARATOR +
                StringUtils.chop(StringUtils.substringAfterLast(between, "_")) +
                SEPARATOR +
                StringUtils.substringBeforeLast(between, "_") +
                SEPARATOR +
                SUFFIX;
    }
}
