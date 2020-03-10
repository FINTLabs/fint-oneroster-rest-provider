package no.fint.oneroster.factory;

import no.fint.model.resource.utdanning.elev.ElevResource;
import no.fint.model.resource.utdanning.elev.ElevforholdResource;
import no.fint.model.resource.utdanning.elev.SkoleressursResource;
import no.fint.model.resource.utdanning.elev.UndervisningsforholdResource;
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResource;
import no.fint.model.utdanning.basisklasser.Gruppe;
import no.fint.oneroster.model.Enrollment;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.model.vocab.RoleType;

public final class EnrollmentFactory {

    private EnrollmentFactory() {
    }

    public static <T extends Gruppe> Enrollment student(ElevforholdResource elevforholdResource, ElevResource elevResource, T gruppe, SkoleResource skoleResource) {
        return new Enrollment(
                elevforholdResource.getSystemId().getIdentifikatorverdi() + "_" + gruppe.getSystemId().getIdentifikatorverdi(),
                GUIDRef.of(GUIDType.USER, elevResource.getSystemId().getIdentifikatorverdi()),
                GUIDRef.of(GUIDType.CLASS, gruppe.getSystemId().getIdentifikatorverdi()),
                GUIDRef.of(GUIDType.ORG, skoleResource.getSystemId().getIdentifikatorverdi()),
                RoleType.STUDENT
        );
    }

    public static <T extends Gruppe> Enrollment teacher(UndervisningsforholdResource undervisningsforholdResource, SkoleressursResource skoleressursResource, T gruppe, SkoleResource skoleResource) {
        return new Enrollment(
                undervisningsforholdResource.getSystemId().getIdentifikatorverdi() + "_" + gruppe.getSystemId().getIdentifikatorverdi(),
                GUIDRef.of(GUIDType.USER, skoleressursResource.getSystemId().getIdentifikatorverdi()),
                GUIDRef.of(GUIDType.CLASS, gruppe.getSystemId().getIdentifikatorverdi()),
                GUIDRef.of(GUIDType.ORG, skoleResource.getSystemId().getIdentifikatorverdi()),
                RoleType.TEACHER
        );
    }
}
