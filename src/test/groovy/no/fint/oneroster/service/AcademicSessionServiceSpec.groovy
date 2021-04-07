package no.fint.oneroster.service

import no.fint.oneroster.model.AcademicSession
import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.repository.OneRosterRepository
import spock.lang.Specification

import java.time.LocalDate
import java.time.Year

class AcademicSessionServiceSpec extends Specification {

    OneRosterRepository repository = Mock()

    AcademicSessionService academicSessionService = new AcademicSessionService(repository)

    def "getAllAcademicSessions returns a list of academicSessions"() {
        when:
        def academicSessions = academicSessionService.getAllAcademicSessions()

        then:
        1 * repository.getAcademicSessions() >> [getTerm()]
        academicSessions.size() == 1
        academicSessions.first().sourcedId == 'term-sourced-id'
        academicSessions.first().title == 'Term'
        academicSessions.first().startDate == LocalDate.of(2020, 8, 1)
        academicSessions.first().endDate == LocalDate.of(2020, 12, 31)
        academicSessions.first().type == SessionType.TERM
        academicSessions.first().schoolYear == Year.of(2021)
    }

    def "getAcademicSession returns an academicSession given valid sourcedId"() {
        when:
        def academicSession = academicSessionService.getAcademicSession('term-sourced-id')

        then:
        1 * repository.getAcademicSessionById(_ as String) >> getTerm()
        academicSession.sourcedId == 'term-sourced-id'
        academicSession.title == 'Term'
        academicSession.startDate == LocalDate.of(2020, 8, 1)
        academicSession.endDate == LocalDate.of(2020, 12, 31)
        academicSession.type == SessionType.TERM
        academicSession.schoolYear == Year.of(2021)
    }

    def "getAllTerms returns a list of terms"() {
        when:
        def terms = academicSessionService.getAllTerms()

        then:
        1 * repository.getAcademicSessions() >> [getTerm()]
        terms.size() == 1
        terms.first().sourcedId == 'term-sourced-id'
        terms.first().title == 'Term'
        terms.first().startDate == LocalDate.of(2020, 8, 1)
        terms.first().endDate == LocalDate.of(2020, 12, 31)
        terms.first().type == SessionType.TERM
        terms.first().schoolYear == Year.of(2021)
    }

    def "getTerm returns a term given valid sourcedId"() {
        when:
        def term = academicSessionService.getAcademicSession('term-sourced-id')

        then:
        1 * repository.getAcademicSessionById(_ as String) >> getTerm()

        term.sourcedId == 'term-sourced-id'
        term.title == 'Term'
        term.startDate == LocalDate.of(2020, 8, 1)
        term.endDate == LocalDate.of(2020, 12, 31)
        term.type == SessionType.TERM
        term.schoolYear == Year.of(2021)
    }

    AcademicSession getTerm() {
        return new AcademicSession(
                'term-sourced-id',
                'Term',
                LocalDate.of(2020, 8, 1),
                LocalDate.of(2020, 12, 31),
                SessionType.TERM,
                Year.of(2021)
        )
    }
}
