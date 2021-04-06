package no.fint.oneroster.util;

import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.utdanning.basisklasser.GruppeResource;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class FactoryUtil {

    public static StatusType getStatusType(GruppeResource group, ZonedDateTime zonedDateTime) {
        List<Periode> period = group.getPeriode();

        if (period == null || period.isEmpty()) {
            return StatusType.ACTIVE;
        }

        boolean active = period.stream()
                .findFirst()
                .filter(begin -> begin.getStart() != null && begin.getStart().compareTo(Date.from(zonedDateTime.toInstant())) <= 0)
                .filter(end -> end.getSlutt() == null || end.getSlutt().compareTo(Date.from(zonedDateTime.toInstant())) >= 0)
                .isPresent();

        return active ? StatusType.ACTIVE : StatusType.TOBEDELETED;
    }
}
