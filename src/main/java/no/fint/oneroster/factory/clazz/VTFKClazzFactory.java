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

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VTFKClazzFactory implements ClazzFactory {
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
        String between = StringUtils.substringBetween(name, "_", "@");
        String school = StringUtils.substringAfterLast(between, "_");
        String group = StringUtils.substringBeforeLast(between, "_");

        return schools.getOrDefault(school, school) +
                SEPARATOR +
                group +
                SEPARATOR +
                "Klasse";
    }

    private static final Map<String, String> schools = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>("BAMVS", "OF-BAV"),
            new AbstractMap.SimpleImmutableEntry<>("BOEVS", "OF-BOV"),
            new AbstractMap.SimpleImmutableEntry<>("FRVS", "OF-FRV"),
            new AbstractMap.SimpleImmutableEntry<>("GRVS", "OF-GRV"),
            new AbstractMap.SimpleImmutableEntry<>("KLOVS", "OF-HJV"),
            new AbstractMap.SimpleImmutableEntry<>("HLVS", "OF-HOLV"),
            new AbstractMap.SimpleImmutableEntry<>("HVS", "OF-HORV"),
            new AbstractMap.SimpleImmutableEntry<>("HVS-NETT", "OF-NETT"),
            new AbstractMap.SimpleImmutableEntry<>("KBV", "OF-KB"),
            new AbstractMap.SimpleImmutableEntry<>("KRAVS", "OF-KRV"),
            new AbstractMap.SimpleImmutableEntry<>("MVS", "OF-MEV"),
            new AbstractMap.SimpleImmutableEntry<>("NOMVS", "OF-NOMV"),
            new AbstractMap.SimpleImmutableEntry<>("NOTVS", "OF-NOV"),
            new AbstractMap.SimpleImmutableEntry<>("NVS", "OF-NTV"),
            new AbstractMap.SimpleImmutableEntry<>("PORVS", "OF-POV"),
            new AbstractMap.SimpleImmutableEntry<>("RVS", "OF-REV"),
            new AbstractMap.SimpleImmutableEntry<>("RJUVS", "OF-RJV"),
            new AbstractMap.SimpleImmutableEntry<>("SVS", "OF-SANV"),
            //new AbstractMap.SimpleImmutableEntry<>("SFVS", "OF-SFH"), Sandefjord folkehøyskole
            new AbstractMap.SimpleImmutableEntry<>("SFVS", "OF-SFV"),
            new AbstractMap.SimpleImmutableEntry<>("SKIVS", "OF-SKIV"),
            new AbstractMap.SimpleImmutableEntry<>("SKOVS", "OF-SKOV"),
            new AbstractMap.SimpleImmutableEntry<>("MVS-SMIH", "OF-SMI"),
            new AbstractMap.SimpleImmutableEntry<>("THVS", "OF-THV"),
            new AbstractMap.SimpleImmutableEntry<>("DALVS", "OF-VTV"),
            new AbstractMap.SimpleImmutableEntry<>("TELVS", "NIK-FAGS"),
            new AbstractMap.SimpleImmutableEntry<>("FIV", "NIK-FAGS"))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
}
