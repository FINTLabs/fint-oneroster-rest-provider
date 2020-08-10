package no.fint.oneroster.util;

import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.utdanning.basisklasser.Gruppe;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class FactoryUtil {

    public static StatusType getStatusType(Gruppe group, ZonedDateTime zonedDateTime) {
        List<Periode> period = group.getPeriode();

        if (period.isEmpty()) return StatusType.ACTIVE;

        boolean active = period.stream()
                .findFirst()
                .filter(begin -> {
                    if (begin.getStart() == null) return false;
                    return begin.getStart().compareTo(Date.from(zonedDateTime.toInstant())) <= 0;
                })
                .filter(end -> {
                    if (end.getSlutt() == null) return true;
                    return end.getSlutt().compareTo(Date.from(zonedDateTime.toInstant())) >= 0;
                }).isPresent();

        return active ? StatusType.ACTIVE : StatusType.TOBEDELETED;
    }
}
