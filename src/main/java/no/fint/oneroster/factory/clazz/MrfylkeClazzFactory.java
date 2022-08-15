package no.fint.oneroster.factory.clazz;

import no.fint.model.felles.kompleksedatatyper.Identifikator;
import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;

import java.util.Map;
import java.util.Optional;

public class MrfylkeClazzFactory implements ClazzFactory {

    @Override
    public String basisGroupNameConverter(BasisgruppeResource basisGroup, SkoleResource school) {
        return basisGroup.getNavn() + " " +
                schoolAbbreviations.getOrDefault(school.getSkolenummer().getIdentifikatorverdi(), getValue(school));
    }

    @Override
    public String teachingGroupNameConverter(UndervisningsgruppeResource teachingGroup, SkoleResource school, FagResource subject) {
        return subject.getNavn() + " " + teachingGroup.getNavn() + " " +
                schoolAbbreviations.getOrDefault(school.getSkolenummer().getIdentifikatorverdi(), getValue(school));
    }

    private String getValue(SkoleResource school) {
        return Optional.ofNullable(school.getOrganisasjonsnummer())
                .map(Identifikator::getIdentifikatorverdi)
                .map(schoolAbbreviations::get)
                .orElse("<Undefined>");
    }

    private final Map<String, String> schoolAbbreviations = Map.ofEntries(
            Map.entry("15001", "VATL"), //Atlanten
            Map.entry("15008", "VGJE"), //Gjermundnes
            Map.entry("15010", "VHAR"), //Haram
            Map.entry("15011", "VHER"), //Herøy
            Map.entry("15007", "VHUS"), //Hustadvika
            Map.entry("15012", "VKRI"), //Kristiansund
            Map.entry("15014", "VMOL"), //Molde
            Map.entry("15018", "VRAU"), //Rauma
            Map.entry("15019", "VROM"), //Romsdal
            Map.entry("15020", "VSPJ"), //Spjelkavik
            Map.entry("15021", "VSTR"), //Stranda
            Map.entry("15023", "VSUN"), //Sunndal
            Map.entry("15024", "VSUR"), //Surnadal
            Map.entry("15025", "VSYK"), //Sykkylven
            Map.entry("15026", "VTIN"), //Tingvoll
            Map.entry("15027", "VULS"), //Ulstein
            Map.entry("15029", "VVOL"), //Volda
            Map.entry("15031", "VORS"), //Ørsta
            Map.entry("15037", "VALE"), //Ålesund
            Map.entry("15035", "VBOR"), //Borgund
            Map.entry("913587987", "KRIFAG"), //Fagskolen i Kristiansund
            Map.entry("874576662", "FIALS")); //Fagskolen i Ålesund
}