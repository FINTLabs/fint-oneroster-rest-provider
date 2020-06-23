package no.fint.oneroster.factory.clazz;

import no.fint.model.utdanning.basisklasser.Gruppe;
import org.apache.commons.lang3.StringUtils;

public class MRFylkeClazzFactory implements ClazzFactory {

    @Override
    public String basisGroupNameConverter(Gruppe basisGroup) {
        String between = StringUtils.substringBetween(basisGroup.getSystemId().getIdentifikatorverdi(), "_", "@");
        String school = StringUtils.substringAfterLast(between, "_");

        return String.join(" ", basisGroup.getNavn(), school);
    }

    @Override
    public String teachingGroupNameConverter(Gruppe teachingGroup) {
        String between = StringUtils.substringBetween(teachingGroup.getSystemId().getIdentifikatorverdi(), "_", "@");
        String school = StringUtils.substringAfterLast(between, "_");
        String subject = StringUtils.substringBetween(teachingGroup.getBeskrivelse(), " i ", " ved ");

        return String.join(" ", subject, teachingGroup.getNavn(), school);
    }
}
