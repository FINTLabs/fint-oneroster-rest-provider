package no.fint.oneroster.service

import no.fint.oneroster.model.AcademicSession
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.repository.FintRepository
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

import java.time.LocalDate

class ClazzServiceSpec extends Specification {

    FintRepository fintRepository = Mock {
        getBasisGroups(_ as String) >> [('/basis-group-sourced-id'): FintObjectFactory.newBasisGroup()]
        getTeachingGroups(_ as String) >> [('/teaching-group-sourced-id'): FintObjectFactory.newTeachingGroup()]
        getLevels(_ as String) >> [('/level-sourced-id'): FintObjectFactory.newLevel()]
        getSubjects(_ as String) >> [('/subject-sourced-id'): FintObjectFactory.newSubject()]
        getSchools(_ as String) >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
    }

    AcademicSessionService academicSessionService = Mock()

    AcademicSessionService.SchoolYear schoolYear = academicSessionService.getSchoolYear(LocalDate.now())

    ClazzService clazzService = new ClazzService(fintRepository, academicSessionService)

    def "getAllClazzes returns a list of clazzes given valid orgId"() {
        when:
        def clazzes = clazzService.getAllClazzes(_ as String)

        then:
        1 * academicSessionService.getAllTerms(_ as String) >> getAcademicSession()

        clazzes.size() == 2

        clazzes.first().sourcedId == 'basis-group-sourced-id'
        clazzes.first().title == 'Basis group'
        clazzes.first().clazzType == ClazzType.HOMEROOM
        clazzes.first().course.sourcedId == 'level-sourced-id'
        clazzes.first().school.sourcedId == 'school-sourced-id'
        clazzes.first().terms.first().sourcedId == 'orgId-1-termin-' + schoolYear.begin + schoolYear.end

        clazzes.last().sourcedId == 'teaching-group-sourced-id'
        clazzes.last().title == 'Teaching group'
        clazzes.last().clazzType == ClazzType.SCHEDULED
        clazzes.last().course.sourcedId == 'subject-sourced-id'
        clazzes.last().school.sourcedId == 'school-sourced-id'
        clazzes.last().terms.last().sourcedId == 'orgId-2-termin-' + schoolYear.begin + schoolYear.end
    }

    def "getClazz returns a clazz given valid orgId and sourcedId"() {
        when:
        def clazz = clazzService.getClazz(_ as String, 'basis-group-sourced-id')

        then:
        1 * academicSessionService.getAllTerms(_ as String) >> getAcademicSession()

        clazz.sourcedId == 'basis-group-sourced-id'
        clazz.title == 'Basis group'
        clazz.clazzType == ClazzType.HOMEROOM
        clazz.course.sourcedId == 'level-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.first().sourcedId == 'orgId-1-termin-' + schoolYear.begin + schoolYear.end
    }

    List<AcademicSession> getAcademicSession() {
        AcademicSession fall = new AcademicSession(
                'orgId-1-termin-' + schoolYear.begin + schoolYear.end,
                '1. termin ' + schoolYear.begin + '/' + schoolYear.end,
                LocalDate.of(schoolYear.begin, 8, 1),
                LocalDate.of(schoolYear.begin, 12, 31),
                SessionType.TERM,
                schoolYear.end
        )

        AcademicSession spring = new AcademicSession(
                'orgId-2-termin-' + schoolYear.begin + schoolYear.end,
                '2. termin ' + schoolYear.begin + '/' + schoolYear.end,
                LocalDate.of(schoolYear.end, 1, 1),
                LocalDate.of(schoolYear.end, 7, 31),
                SessionType.TERM,
                schoolYear.end
        )

        return [fall, spring]
    }
}