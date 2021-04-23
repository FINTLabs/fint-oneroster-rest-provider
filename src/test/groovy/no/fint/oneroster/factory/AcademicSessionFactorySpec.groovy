package no.fint.oneroster.factory

import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.FintObjectFactory
import spock.lang.Specification

import java.time.LocalDate
import java.time.Year

class AcademicSessionFactorySpec extends Specification {

    def "term() returns academic session object"() {
        when:
        def term = AcademicSessionFactory.term(FintObjectFactory.newTerm(), FintObjectFactory.newSchoolYear())

        then:
        term.sourcedId == 'term-sourced-id'
        term.type == SessionType.TERM
        term.title == 'Term'
        term.startDate == LocalDate.parse('2020-08-01')
        term.endDate == LocalDate.parse('2020-12-31')
        term.schoolYear == Year.of(2021)
    }
}