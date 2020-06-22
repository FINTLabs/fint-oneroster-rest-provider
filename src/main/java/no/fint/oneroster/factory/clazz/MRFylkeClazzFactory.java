package no.fint.oneroster.factory.clazz;

import no.fint.model.utdanning.basisklasser.Gruppe;
import org.apache.commons.lang3.StringUtils;

public class MRFylkeClazzFactory implements ClazzFactory {

    @Override
    public String basisGroupNameConverter(Gruppe basisGroup) {
        String school = StringUtils.substringAfterLast(basisGroup.getBeskrivelse(), "ved ");

        return String.join(" ", "Basisgruppe", basisGroup.getNavn(), school);
    }

    @Override
    public String teachingGroupNameConverter(Gruppe teachingGroup) {
        String subject = StringUtils.substringBetween(teachingGroup.getBeskrivelse(), " i ", " ved ");
        String school = StringUtils.substringAfterLast(teachingGroup.getBeskrivelse(), "ved ");

        return String.join(" ", subject, teachingGroup.getNavn(), school);
    }
}
