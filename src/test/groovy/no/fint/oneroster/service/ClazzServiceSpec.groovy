package no.fint.oneroster.service

import no.fint.oneroster.model.AcademicSession
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.repository.FintEducationService
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

import java.time.LocalDate
import java.time.Year

class ClazzServiceSpec extends Specification {

    FintEducationService fintEducationService = Mock {
        getBasisGroups() >> [('/basis-group-sourced-id'): FintObjectFactory.newBasisGroup()]
        getTeachingGroups() >> [('/teaching-group-sourced-id'): FintObjectFactory.newTeachingGroup()]
        getLevels() >> [('/level-sourced-id'): FintObjectFactory.newLevel()]
        getSubjects() >> [('/subject-sourced-id'): FintObjectFactory.newSubject()]
        getSchools() >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
    }

    AcademicSessionService academicSessionService = Mock()

    ClazzService clazzService = new ClazzService(fintEducationService, academicSessionService)

    def "getAllClazzes returns a list of clazzes given valid orgId"() {
        when:
        def clazzes = clazzService.getAllClazzes()

        then:
        1 * academicSessionService.getAllTerms() >> getAcademicSession()

        clazzes.size() == 2

        clazzes.first().sourcedId == 'basis-group-sourced-id'
        clazzes.first().title == 'Basis group'
        clazzes.first().classType == ClazzType.HOMEROOM
        clazzes.first().course.sourcedId == 'level-sourced-id'
        clazzes.first().school.sourcedId == 'school-sourced-id'
        clazzes.first().terms.size() == 2
        clazzes.first().terms.first().sourcedId == 'term2019-08-012019-12-31'

        clazzes.last().sourcedId == 'teaching-group-sourced-id'
        clazzes.last().title == 'Teaching group'
        clazzes.last().classType == ClazzType.SCHEDULED
        clazzes.last().course.sourcedId == 'subject-sourced-id'
        clazzes.last().school.sourcedId == 'school-sourced-id'
        clazzes.last().terms.size() == 2
        clazzes.last().terms.last().sourcedId == 'term2020-01-012020-07-31'
    }

    def "getClazz returns a clazz given valid orgId and sourcedId"() {
        when:
        def clazz = clazzService.getClazz('basis-group-sourced-id')

        then:
        1 * academicSessionService.getAllTerms() >> getAcademicSession()

        clazz.sourcedId == 'basis-group-sourced-id'
        clazz.title == 'Basis group'
        clazz.classType == ClazzType.HOMEROOM
        clazz.course.sourcedId == 'level-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.size() == 2
        clazz.terms.first().sourcedId == 'term2019-08-012019-12-31'
    }

    List<AcademicSession> getAcademicSession() {
        AcademicSession firstTerm = new AcademicSession(
                'term2019-08-012019-12-31',
                '1 termin 2019/2020',
                LocalDate.of(2019, 8, 1),
                LocalDate.of(2010, 12, 31),
                SessionType.TERM,
                Year.of(2020)
        )

        AcademicSession secondTerm = new AcademicSession(
                'term2020-01-012020-07-31',
                '2 termin 2019/2020',
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 7, 31),
                SessionType.TERM,
                Year.of(2020)
        )

        return [firstTerm, secondTerm]
    }
}