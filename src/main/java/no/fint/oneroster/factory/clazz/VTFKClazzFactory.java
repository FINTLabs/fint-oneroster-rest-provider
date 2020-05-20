package no.fint.oneroster.factory.clazz;

import no.fint.model.utdanning.basisklasser.Gruppe;
import org.apache.commons.lang3.StringUtils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VTFKClazzFactory implements ClazzFactory {
    private final String SEPARATOR = "-";

    @Override
    public String basisGroupNameConverter(Gruppe basisGroup) {
        String between = StringUtils.substringBetween(basisGroup.getSystemId().getIdentifikatorverdi(), "_", "@");
        String school = StringUtils.substringAfterLast(between, "_");
        String group = StringUtils.substringBetween(basisGroup.getBeskrivelse(), "Basisgruppe ", " ved ");

        return schools.getOrDefault(school, school) +
                SEPARATOR +
                group +
                SEPARATOR +
                "Klasse";
    }

    @Override
    public String teachingGroupNameConverter(Gruppe teachingGroup) {
        String between = StringUtils.substringBetween(teachingGroup.getSystemId().getIdentifikatorverdi(), "_", "@");
        String school = StringUtils.substringAfterLast(between, "_");
        String group = StringUtils.substringBetween(teachingGroup.getBeskrivelse(), "Undervisningsgruppa ", " i ");
        String subject = StringUtils.substringBetween(teachingGroup.getBeskrivelse(), " i ", " ved ");

        return schools.getOrDefault(school, school) +
                    SEPARATOR +
                    StringUtils.substringBeforeLast(group, "_") +
                    SEPARATOR +
                    subject;
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
