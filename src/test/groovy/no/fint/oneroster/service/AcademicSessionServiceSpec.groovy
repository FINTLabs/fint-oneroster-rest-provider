package no.fint.oneroster.service

import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.repository.FintRepository
import spock.lang.Ignore
import spock.lang.Specification

import java.time.LocalDate

class AcademicSessionServiceSpec extends Specification {

    FintRepository fintRepository = Mock {
    }

    AcademicSessionService academicSessionService = new AcademicSessionService(fintRepository)

    AcademicSessionService.SchoolYear schoolYear = academicSessionService.getSchoolYear(LocalDate.now())

    def "getAllAcademicSessions returns a list of academicSessions given valid orgId"() {
        when:
        def academicSessions = academicSessionService.getAllAcademicSessions('orgId')

        then:
        academicSessions.size() == 2

        academicSessions.first().sourcedId == 'orgId-1-termin-' + schoolYear.begin + schoolYear.end
        academicSessions.first().title == '1. termin ' + schoolYear.begin + '/' + schoolYear.end
        academicSessions.first().startDate == LocalDate.of(schoolYear.begin, 8, 1)
        academicSessions.first().endDate == LocalDate.of(schoolYear.begin, 12, 31)
        academicSessions.first().type == SessionType.TERM
        academicSessions.first().schoolYear == schoolYear.end

        academicSessions.last().sourcedId == 'orgId-2-termin-' + schoolYear.begin + schoolYear.end
        academicSessions.last().title == '2. termin ' + schoolYear.begin + '/' + schoolYear.end
        academicSessions.last().startDate == LocalDate.of(schoolYear.end, 1, 1)
        academicSessions.last().endDate == LocalDate.of(schoolYear.end, 7, 31)
        academicSessions.last().type == SessionType.TERM
        academicSessions.last().schoolYear == schoolYear.end
    }

    def "getAcademicSession returns an academicSession given valid orgId and sourcedId"() {
        given:
        def sourcedId = 'orgId-1-termin-' + schoolYear.begin + schoolYear.end

        when:
        def academicSession = academicSessionService.getAcademicSession('orgId', sourcedId)

        then:
        academicSession.sourcedId == 'orgId-1-termin-' + schoolYear.begin + schoolYear.end
        academicSession.title == '1. termin ' + schoolYear.begin + '/' + schoolYear.end
        academicSession.startDate == LocalDate.of(schoolYear.begin, 8, 1)
        academicSession.endDate == LocalDate.of(schoolYear.begin, 12, 31)
        academicSession.type == SessionType.TERM
        academicSession.schoolYear == schoolYear.end
    }

    def "getAllTerms returns a list of terms given valid orgId"() {
        when:
        def terms = academicSessionService.getAllTerms('orgId')

        then:
        terms.size() == 2

        terms.first().sourcedId == 'orgId-1-termin-' + schoolYear.begin + schoolYear.end
        terms.first().title == '1. termin ' + schoolYear.begin + '/' + schoolYear.end
        terms.first().startDate == LocalDate.of(schoolYear.begin, 8, 1)
        terms.first().endDate == LocalDate.of(schoolYear.begin, 12, 31)
        terms.first().type == SessionType.TERM
        terms.first().schoolYear == schoolYear.end

        terms.last().sourcedId == 'orgId-2-termin-' + schoolYear.begin + schoolYear.end
        terms.last().title == '2. termin ' + schoolYear.begin + '/' + schoolYear.end
        terms.last().startDate == LocalDate.of(schoolYear.end, 1, 1)
        terms.last().endDate == LocalDate.of(schoolYear.end, 7, 31)
        terms.last().type == SessionType.TERM
        terms.last().schoolYear == schoolYear.end
    }

    def "getTerm returns a term given valid orgId and sourcedId"() {
        given:
        def sourcedId = 'orgId-1-termin-' + schoolYear.begin + schoolYear.end

        when:
        def academicSession = academicSessionService.getAcademicSession('orgId', sourcedId)

        then:
        academicSession.sourcedId == 'orgId-1-termin-' + schoolYear.begin + schoolYear.end
        academicSession.title == '1. termin ' + schoolYear.begin + '/' + schoolYear.end
        academicSession.startDate == LocalDate.of(schoolYear.begin, 8, 1)
        academicSession.endDate == LocalDate.of(schoolYear.begin, 12, 31)
        academicSession.type == SessionType.TERM
        academicSession.schoolYear == schoolYear.end
    }

    @Ignore
    def "getAllGradingPeriods returns a list of gradingPeriods given valid orgId"() {
    }

    @Ignore
    def "getGradingPeriod returns a gradingPeriod given valid orgId and sourcedId"() {
    }
}
