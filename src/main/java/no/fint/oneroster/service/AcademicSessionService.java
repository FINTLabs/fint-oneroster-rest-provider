package no.fint.oneroster.service;

import lombok.Getter;
import lombok.Setter;
import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.vocab.SessionType;
import no.fint.oneroster.repository.FintRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AcademicSessionService {

    private final FintRepository fintRepository;

    public AcademicSessionService(FintRepository fintRepository) {
        this.fintRepository = fintRepository;
    }

    public List<AcademicSession> getAllAcademicSessions(String orgId) {
        SchoolYear schoolYear = getSchoolYear(LocalDate.now());

        AcademicSession fallTerm = new AcademicSession(
                orgId + "-1-termin-" + schoolYear.getBegin() + schoolYear.getEnd(),
                "1. termin " + schoolYear.getBegin()  + "/" + schoolYear.getEnd(),
                LocalDate.of(schoolYear.getBegin(),8,1),
                LocalDate.of(schoolYear.getBegin(),12,31),
                SessionType.TERM,
                schoolYear.getEnd());

        AcademicSession springTerm = new AcademicSession(
                orgId + "-2-termin-" + schoolYear.getBegin() + schoolYear.getEnd(),
                "2. termin " + schoolYear.getBegin() + "/" + schoolYear.getEnd(),
                LocalDate.of(schoolYear.getEnd(),1,1),
                LocalDate.of(schoolYear.getEnd(),7,31),
                SessionType.TERM,
                schoolYear.getEnd());

        return Arrays.asList(fallTerm, springTerm);
    }

    public AcademicSession getAcademicSession(String orgId, String sourcedId) {
        return getAllAcademicSessions(orgId).stream()
                .filter(academicSession -> academicSession.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<AcademicSession> getAllTerms(String orgId) {
        return getAllAcademicSessions(orgId).stream()
                .filter(academicSession -> academicSession.getType().equals(SessionType.TERM))
                .collect(Collectors.toList());
    }

    public AcademicSession getTerm(String orgId, String sourcedId) {
        return getAllTerms(orgId).stream()
                .filter(term -> term.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<AcademicSession> getAllGradingPeriods(String orgId) {
        return getAllAcademicSessions(orgId).stream()
                .filter(academicSession -> academicSession.getType().equals(SessionType.GRADINGPERIOD))
                .collect(Collectors.toList());
    }

    public AcademicSession getGradingPeriod(String orgId, String sourcedId) {
        return getAllGradingPeriods(orgId).stream()
                .filter(gradingPeriod -> gradingPeriod.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public static SchoolYear getSchoolYear(LocalDate date) {
        LocalDate fall = LocalDate.of(date.getYear(), Month.AUGUST, 1);

        if (date.isBefore(fall)) {
            return new SchoolYear(date.minusYears(1).getYear(), date.getYear());
        } else {
            return new SchoolYear(date.getYear(), date.plusYears(1).getYear());
        }
    }

    @Getter @Setter
    public static class SchoolYear {
        private final int begin;
        private final int end;

        public SchoolYear(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }
    }
}
