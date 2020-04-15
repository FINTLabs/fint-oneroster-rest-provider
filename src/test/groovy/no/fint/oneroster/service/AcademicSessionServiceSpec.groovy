package no.fint.oneroster.service

import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.properties.OrganisationProperties
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDate
import java.time.Year

class AcademicSessionServiceSpec extends Specification {

    OrganisationProperties organisationProperties = Mock {
        getOrganisation() >> new OrganisationProperties.Organisation(
                schoolYear: new OrganisationProperties.SchoolYear(
                        beginDate: '2019-08-01', endDate: '2020-07-31', name: 'Skoleåret 2019/2020',
                        firstTerm: new OrganisationProperties.Term(
                                beginDate: '2019-08-01', endDate: '2019-12-31', name: '1. termin 2019/2020'),
                        secondTerm: new OrganisationProperties.Term(
                                beginDate: '2020-01-01', endDate: '2020-07-31', name: '2. termin 2019/2020'))
        )
    }

    AcademicSessionService academicSessionService = new AcademicSessionService(organisationProperties)

    def "getAllAcademicSessions returns a list of academicSessions given valid orgId"() {
        when:
        def academicSessions = academicSessionService.getAllAcademicSessions()

        then:
        academicSessions.size() == 3

        academicSessions.first().sourcedId == 'schoolYear2019-08-012020-07-31'
        academicSessions.first().title == 'Skoleåret 2019/2020'
        academicSessions.first().startDate == LocalDate.of(2019, 8, 1)
        academicSessions.first().endDate == LocalDate.of(2020, 7, 31)
        academicSessions.first().type == SessionType.SCHOOLYEAR
        academicSessions.first().schoolYear == Year.of(2020)

        academicSessions.last().sourcedId == 'term2020-01-012020-07-31'
        academicSessions.last().title == '2. termin 2019/2020'
        academicSessions.last().startDate == LocalDate.of(2020, 1, 1)
        academicSessions.last().endDate == LocalDate.of(2020, 7, 31)
        academicSessions.last().type == SessionType.TERM
        academicSessions.last().schoolYear == Year.of(2020)
    }

    def "getAcademicSession returns an academicSession given valid orgId and sourcedId"() {
        given:
        def sourcedId = 'term2019-08-012019-12-31'

        when:
        def academicSession = academicSessionService.getAcademicSession(sourcedId)

        then:
        academicSession.sourcedId == 'term2019-08-012019-12-31'
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

        terms.first().sourcedId == 'term2019-08-012019-12-31'
        terms.first().title == '1. termin 2019/2020'
        terms.first().startDate == LocalDate.of(2019, 8, 1)
        terms.first().endDate == LocalDate.of(2019, 12, 31)
        terms.first().type == SessionType.TERM
        terms.first().schoolYear == Year.of(2020)

        terms.last().sourcedId == 'term2020-01-012020-07-31'
        terms.last().title == '2. termin 2019/2020'
        terms.last().startDate == LocalDate.of(2020, 1, 1)
        terms.last().endDate == LocalDate.of(2020, 7, 31)
        terms.last().type == SessionType.TERM
        terms.last().schoolYear == Year.of(2020)
    }

    def "getTerm returns a term given valid orgId and sourcedId"() {
        given:
        def sourcedId = 'term2019-08-012019-12-31'

        when:
        def academicSession = academicSessionService.getAcademicSession(sourcedId)

        then:
        academicSession.sourcedId == 'term2019-08-012019-12-31'
        academicSession.title == '1. termin 2019/2020'
        academicSession.startDate == LocalDate.of(2019, 8, 1)
        academicSession.endDate == LocalDate.of(2019, 12, 31)
        academicSession.type == SessionType.TERM
        academicSession.schoolYear == Year.of(2020)
    }

    @Ignore
    def "getAllGradingPeriods returns a list of gradingPeriods given valid orgId"() {
    }

    @Ignore
    def "getGradingPeriod returns a gradingPeriod given valid orgId and sourcedId"() {
    }
}
