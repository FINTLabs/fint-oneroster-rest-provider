package no.fint.oneroster.factory;

import no.fint.model.resource.utdanning.basisklasser.GruppeResource;
import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.model.vocab.RoleType;
import no.fint.oneroster.util.FactoryUtil;

import java.time.ZonedDateTime;

import static no.fint.oneroster.util.StringNormalizer.normalize;

public final class EnrollmentFactory {

    private EnrollmentFactory() {
    }

    public static <T extends GruppeResource> Enrollment student(ElevforholdResource elevforholdResource, ElevResource elevResource, T gruppe, SkoleResource skoleResource) {
        return new Enrollment(
                normalize(elevforholdResource.getSystemId().getIdentifikatorverdi() + "_" + gruppe.getSystemId().getIdentifikatorverdi()),
                FactoryUtil.getStatusType(gruppe, ZonedDateTime.now()),
                GUIDRef.of(GUIDType.USER, normalize(elevResource.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.CLASS, normalize(gruppe.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())),
                RoleType.STUDENT
        );
    }

    public static <T extends GruppeResource> Enrollment teacher(UndervisningsforholdResource undervisningsforholdResource, SkoleressursResource skoleressursResource, T gruppe, SkoleResource skoleResource) {
        return new Enrollment(
                normalize(undervisningsforholdResource.getSystemId().getIdentifikatorverdi() + "_" + gruppe.getSystemId().getIdentifikatorverdi()),
                FactoryUtil.getStatusType(gruppe, ZonedDateTime.now()),
                GUIDRef.of(GUIDType.USER, normalize(skoleressursResource.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.CLASS, normalize(gruppe.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())),
                RoleType.TEACHER
        );
    }
}
