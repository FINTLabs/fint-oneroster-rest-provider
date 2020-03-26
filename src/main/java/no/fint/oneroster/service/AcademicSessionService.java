package no.fint.oneroster.service;

import no.fint.oneroster.exception.NotFoundException;
import no.fint.oneroster.model.AcademicSession;
import no.fint.oneroster.model.GUIDRef;
import no.fint.oneroster.model.vocab.GUIDType;
import no.fint.oneroster.model.vocab.SessionType;
import no.fint.oneroster.properties.OrganisationProperties;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AcademicSessionService {

    private final OrganisationProperties organisationProperties;

    public AcademicSessionService(OrganisationProperties organisationProperties) {
        this.organisationProperties = organisationProperties;
    }

    public List<AcademicSession> getAllAcademicSessions(String orgId) {
        OrganisationProperties.Organisation organisation = organisationProperties.getOrganisations().get(orgId);

        Year endYear = Year.of(organisation.getSchoolYear().getEndDate().getYear());

        AcademicSession schoolYear = new AcademicSession(
                orgId + SessionType.SCHOOLYEAR.getSessionType() + organisation.getSchoolYear().getBeginDate() + organisation.getSchoolYear().getEndDate(),
                organisation.getSchoolYear().getName(),
                organisation.getSchoolYear().getBeginDate(),
                organisation.getSchoolYear().getEndDate(),
                SessionType.SCHOOLYEAR,
                endYear
        );

        AcademicSession firstTerm = new AcademicSession(
                orgId + SessionType.TERM.getSessionType() + organisation.getSchoolYear().getFirstTerm().getBeginDate() + organisation.getSchoolYear().getFirstTerm().getEndDate(),
                organisation.getSchoolYear().getFirstTerm().getName(),
                organisation.getSchoolYear().getFirstTerm().getBeginDate(),
                organisation.getSchoolYear().getFirstTerm().getEndDate(),
                SessionType.TERM,
                endYear);

        firstTerm.setParent(GUIDRef.of(GUIDType.ACADEMICSESSION, schoolYear.getSourcedId()));

        AcademicSession secondTerm = new AcademicSession(
                orgId + SessionType.TERM.getSessionType() + organisation.getSchoolYear().getSecondTerm().getBeginDate() + organisation.getSchoolYear().getSecondTerm().getEndDate(),
                organisation.getSchoolYear().getSecondTerm().getName(),
                organisation.getSchoolYear().getSecondTerm().getBeginDate(),
                organisation.getSchoolYear().getSecondTerm().getEndDate(),
                SessionType.TERM,
                endYear);

        secondTerm.setParent(GUIDRef.of(GUIDType.ACADEMICSESSION, secondTerm.getSourcedId()));

        schoolYear.setChildren(Arrays.asList(GUIDRef.of(GUIDType.ACADEMICSESSION, firstTerm.getSourcedId()),
                GUIDRef.of(GUIDType.ACADEMICSESSION, secondTerm.getSourcedId())));

        return Arrays.asList(schoolYear, firstTerm, secondTerm);
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
