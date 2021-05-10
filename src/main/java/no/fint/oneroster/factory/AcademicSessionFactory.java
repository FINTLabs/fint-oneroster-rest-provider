package no.fint.oneroster.factory;

import no.fint.model.felles.kompleksedatatyper.Periode;
import no.fint.model.resource.utdanning.kodeverk.SkolearResource;
import no.fint.model.resource.utdanning.kodeverk.TerminResource;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.vocab.SessionType;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import static no.fint.oneroster.util.StringNormalizer.normalize;

public final class AcademicSessionFactory {

    private AcademicSessionFactory() {
    }

    public static AcademicSession term(TerminResource terminResource, SkolearResource schoolYear) {
        Optional<Periode> period = Optional.ofNullable(terminResource.getGyldighetsperiode());

        return new AcademicSession(
                normalize(terminResource.getSystemId().getIdentifikatorverdi()),
                terminResource.getNavn(),
                period.map(Periode::getStart).map(toLocalDate).orElse(null),
                period.map(Periode::getSlutt).map(toLocalDate).orElse(null),
                SessionType.TERM,
                Optional.ofNullable(schoolYear.getGyldighetsperiode())
                        .map(Periode::getSlutt)
                        .map(toLocalDate)
                        .map(LocalDate::getYear)
                        .map(Year::of)
                        .orElse(null)
        );
    }

    private static final Function<Date, LocalDate> toLocalDate = date -> date.toInstant().atZone(ZoneId.of("Z")).toLocalDate();
}