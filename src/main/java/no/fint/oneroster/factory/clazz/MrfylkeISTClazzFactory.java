package no.fint.oneroster.factory.clazz;

import no.fint.model.resource.utdanning.elev.BasisgruppeResource;
import no.fint.model.resource.utdanning.timeplan.FagResource;
import no.fint.model.resource.utdanning.timeplan.UndervisningsgruppeResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import org.apache.commons.lang3.StringUtils;

public class MrfylkeISTClazzFactory implements ClazzFactory {

    @Override
    public String basisGroupNameConverter(BasisgruppeResource basisGroup, SkoleResource school) {
        String between = StringUtils.substringBetween(basisGroup.getSystemId().getIdentifikatorverdi(), "_", "@");
        String schoolFromIdentifier = StringUtils.substringAfterLast(between, "_");

        return String.join(" ", basisGroup.getNavn(), schoolFromIdentifier);
    }

    @Override
    public String teachingGroupNameConverter(UndervisningsgruppeResource teachingGroup, SkoleResource school, FagResource subject) {
        String between = StringUtils.substringBetween(teachingGroup.getSystemId().getIdentifikatorverdi(), "_", "@");
        String schoolFromIdentifier = StringUtils.substringAfterLast(between, "_");
        String subjectFromDescription = StringUtils.substringBetween(teachingGroup.getBeskrivelse(), " i ", " ved ");

        return String.join(" ", subjectFromDescription, teachingGroup.getNavn(), schoolFromIdentifier);
    }
}
