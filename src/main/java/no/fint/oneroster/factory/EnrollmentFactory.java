package no.fint.oneroster.factory;

import no.fint.model.felles.kompleksedatatyper.Periode;
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
import no.fint.oneroster.model.vocab.StatusType;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static no.fint.oneroster.util.StringNormalizer.normalize;

public final class EnrollmentFactory {

    private EnrollmentFactory() {
    }

    public static <T extends Gruppe> Enrollment student(ElevforholdResource elevforholdResource, ElevResource elevResource, T gruppe, SkoleResource skoleResource) {
        return new Enrollment(
                normalize(elevforholdResource.getSystemId().getIdentifikatorverdi() + "_" + gruppe.getSystemId().getIdentifikatorverdi()),
                getStatusType(gruppe),
                GUIDRef.of(GUIDType.USER, normalize(elevResource.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.CLASS, normalize(gruppe.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())),
                RoleType.STUDENT
        );
    }

    public static <T extends Gruppe> Enrollment teacher(UndervisningsforholdResource undervisningsforholdResource, SkoleressursResource skoleressursResource, T gruppe, SkoleResource skoleResource) {
        return new Enrollment(
                normalize(undervisningsforholdResource.getSystemId().getIdentifikatorverdi() + "_" + gruppe.getSystemId().getIdentifikatorverdi()),
                getStatusType(gruppe),
                GUIDRef.of(GUIDType.USER, normalize(skoleressursResource.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.CLASS, normalize(gruppe.getSystemId().getIdentifikatorverdi())),
                GUIDRef.of(GUIDType.ORG, normalize(skoleResource.getSystemId().getIdentifikatorverdi())),
                RoleType.TEACHER
        );
    }

    private static StatusType getStatusType(Gruppe group) {
        List<Periode> period = group.getPeriode();

        if (period.isEmpty()) return StatusType.ACTIVE;

        boolean active = period.stream()
                .findFirst()
                .filter(begin -> {
                    if (begin.getStart() == null) return false;
                    return begin.getStart().compareTo(Date.from(ZonedDateTime.now().toInstant())) <= 0;
                })
                .filter(end -> {
                    if (end.getSlutt() == null) return true;
                    return end.getSlutt().compareTo(Date.from(ZonedDateTime.now().toInstant())) >= 0;
                }).isPresent();

        return active ? StatusType.ACTIVE : StatusType.TOBEDELETED;
    }
}
