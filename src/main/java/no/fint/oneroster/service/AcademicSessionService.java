package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.model.vocab.SessionType;
import no.fint.oneroster.properties.OneRosterProperties;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AcademicSessionService {
    private final OneRosterProperties oneRosterProperties;

    public AcademicSessionService(OneRosterProperties oneRosterProperties) {
        this.oneRosterProperties = oneRosterProperties;
    }

    public List<AcademicSession> getAllAcademicSessions() {
        OneRosterProperties.AcademicSession academicSession = oneRosterProperties.getAcademicSession();

        Year endYear = Year.of(academicSession.getSchoolYear().getEndDate().getYear());

        AcademicSession schoolYear = new AcademicSession(
                academicSession.getSchoolYear().getSourcedId(),
                academicSession.getSchoolYear().getName(),
                academicSession.getSchoolYear().getBeginDate(),
                academicSession.getSchoolYear().getEndDate(),
                SessionType.SCHOOLYEAR,
                endYear
        );

        AcademicSession firstTerm = new AcademicSession(
                academicSession.getFirstTerm().getSourcedId(),
                academicSession.getFirstTerm().getName(),
                academicSession.getFirstTerm().getBeginDate(),
                academicSession.getFirstTerm().getEndDate(),
                SessionType.TERM,
                endYear);

        firstTerm.setParent(GUIDRef.of(GUIDType.ACADEMICSESSION, schoolYear.getSourcedId()));

        AcademicSession secondTerm = new AcademicSession(
                academicSession.getSecondTerm().getSourcedId(),
                academicSession.getSecondTerm().getName(),
                academicSession.getSecondTerm().getBeginDate(),
                academicSession.getSecondTerm().getEndDate(),
                SessionType.TERM,
                endYear);

        secondTerm.setParent(GUIDRef.of(GUIDType.ACADEMICSESSION, schoolYear.getSourcedId()));

        schoolYear.setChildren(Arrays.asList(GUIDRef.of(GUIDType.ACADEMICSESSION, firstTerm.getSourcedId()),
                GUIDRef.of(GUIDType.ACADEMICSESSION, secondTerm.getSourcedId())));

        return Arrays.asList(schoolYear, firstTerm, secondTerm);
    }

    public AcademicSession getAcademicSession(String sourcedId) {
        return getAllAcademicSessions()
                .stream()
                .filter(academicSession -> academicSession.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<AcademicSession> getAllTerms() {
        return getAllAcademicSessions()
                .stream()
                .filter(academicSession -> academicSession.getType().equals(SessionType.TERM))
                .collect(Collectors.toList());
    }

    public AcademicSession getTerm(String sourcedId) {
        return getAllTerms()
                .stream()
                .filter(term -> term.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public List<AcademicSession> getAllGradingPeriods() {
        return getAllAcademicSessions()
                .stream()
                .filter(academicSession -> academicSession.getType().equals(SessionType.GRADINGPERIOD))
                .collect(Collectors.toList());
    }

    public AcademicSession getGradingPeriod(String sourcedId) {
        return getAllGradingPeriods()
                .stream()
                .filter(gradingPeriod -> gradingPeriod.getSourcedId().equals(sourcedId))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    /*
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
        private final Year begin;
        private final Year end;

        public SchoolYear(int begin, int end) {
            this.begin = Year.of(begin);
            this.end = Year.of(end);
        }
    }
     */
}
