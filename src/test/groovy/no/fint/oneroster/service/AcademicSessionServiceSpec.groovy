package no.fint.oneroster.service

import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.properties.OneRosterProperties
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDate
import java.time.Year

class AcademicSessionServiceSpec extends Specification {

    OneRosterProperties oneRosterProperties = Mock {
        getAcademicSession() >> new OneRosterProperties.AcademicSession(
                schoolYear: new OneRosterProperties.SchoolYear(sourcedId: 'SY20192020', beginDate: '2019-08-01', endDate: '2020-07-31', name: 'Skoleåret 2019/2020'),
                firstTerm: new OneRosterProperties.Term(sourcedId: 'T1SY20192020', beginDate: '2019-08-01', endDate: '2019-12-31', name: '1. termin 2019/2020'),
                secondTerm: new OneRosterProperties.Term(sourcedId: 'T2SY20192020', beginDate: '2020-01-01', endDate: '2020-07-31', name: '2. termin 2019/2020')
        )
    }

    AcademicSessionService academicSessionService = new AcademicSessionService(oneRosterProperties)

    def "getAllAcademicSessions returns a list of academicSessions given valid orgId"() {
        when:
        def academicSessions = academicSessionService.getAllAcademicSessions()

        then:
        academicSessions.size() == 3

        academicSessions.first().sourcedId == 'SY20192020'
        academicSessions.first().title == 'Skoleåret 2019/2020'
        academicSessions.first().startDate == LocalDate.of(2019, 8, 1)
        academicSessions.first().endDate == LocalDate.of(2020, 7, 31)
        academicSessions.first().type == SessionType.SCHOOLYEAR
        academicSessions.first().schoolYear == Year.of(2020)

        academicSessions.last().sourcedId == 'T2SY20192020'
        academicSessions.last().title == '2. termin 2019/2020'
        academicSessions.last().startDate == LocalDate.of(2020, 1, 1)
        academicSessions.last().endDate == LocalDate.of(2020, 7, 31)
        academicSessions.last().type == SessionType.TERM
        academicSessions.last().schoolYear == Year.of(2020)
    }

    def "getAcademicSession returns an academicSession given valid orgId and sourcedId"() {
        given:
        def sourcedId = 'T1SY20192020'

        when:
        def academicSession = academicSessionService.getAcademicSession(sourcedId)

        then:
        academicSession.sourcedId == 'T1SY20192020'
        academicSession.title == '1. termin 2019/2020'
        academicSession.startDate == LocalDate.of(2019, 8, 1)
        academicSession.endDate == LocalDate.of(2019, 12, 31)
        academicSession.type == SessionType.TERM
        academicSession.schoolYear == Year.of(2020)
    }

    def "getAllTerms returns a list of terms given valid orgId"() {
        when:
        def terms = academicSessionService.getAllTerms()

        then:
        terms.size() == 2

        terms.first().sourcedId == 'T1SY20192020'
        terms.first().title == '1. termin 2019/2020'
        terms.first().startDate == LocalDate.of(2019, 8, 1)
        terms.first().endDate == LocalDate.of(2019, 12, 31)
        terms.first().type == SessionType.TERM
        terms.first().schoolYear == Year.of(2020)

        terms.last().sourcedId == 'T2SY20192020'
        terms.last().title == '2. termin 2019/2020'
        terms.last().startDate == LocalDate.of(2020, 1, 1)
        terms.last().endDate == LocalDate.of(2020, 7, 31)
        terms.last().type == SessionType.TERM
        terms.last().schoolYear == Year.of(2020)
    }

    def "getTerm returns a term given valid orgId and sourcedId"() {
        given:
        def sourcedId = 'T1SY20192020'

        when:
        def term = academicSessionService.getAcademicSession(sourcedId)

        then:
        term.sourcedId == 'T1SY20192020'
        term.title == '1. termin 2019/2020'
        term.startDate == LocalDate.of(2019, 8, 1)
        term.endDate == LocalDate.of(2019, 12, 31)
        term.type == SessionType.TERM
        term.schoolYear == Year.of(2020)
    }

    @Ignore
    def "getAllGradingPeriods returns a list of gradingPeriods given valid orgId"() {
    }

    @Ignore
    def "getGradingPeriod returns a gradingPeriod given valid orgId and sourcedId"() {
    }
}
