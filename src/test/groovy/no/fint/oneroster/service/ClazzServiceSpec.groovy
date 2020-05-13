package no.fint.oneroster.service

import no.fint.oneroster.model.AcademicSession
import no.fint.oneroster.model.Clazz
import no.fint.oneroster.model.GUIDRef
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.model.vocab.GUIDType
import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.repository.OneRosterService
import spock.lang.Specification

import java.time.LocalDate
import java.time.Year

class ClazzServiceSpec extends Specification {

    OneRosterService oneRosterService = Mock {
        getAllClazzes() >> getClazzes()
    }

    ClazzService clazzService = new ClazzService(oneRosterService)

    def "getAllClazzes returns a list of clazzes"() {
        when:
        def clazzes = clazzService.getAllClazzes()

        then:
        clazzes.size() == 2
        clazzes.first().sourcedId == 'basis-group-sourced-id'
        clazzes.first().title == 'Basis group'
        clazzes.first().classType == ClazzType.HOMEROOM
        clazzes.first().course.sourcedId == 'level-sourced-id'
        clazzes.first().school.sourcedId == 'school-sourced-id'
        clazzes.first().terms.size() == 1
        clazzes.first().terms.first().sourcedId == 'T1SY20192020'

        clazzes.last().sourcedId == 'teaching-group-sourced-id'
        clazzes.last().title == 'Teaching group'
        clazzes.last().classType == ClazzType.SCHEDULED
        clazzes.last().course.sourcedId == 'subject-sourced-id'
        clazzes.last().school.sourcedId == 'school-sourced-id'
        clazzes.last().terms.size() == 1
        clazzes.last().terms.last().sourcedId == 'T1SY20192020'
    }

    def "getClazz returns a clazz given valid sourcedId"() {
        when:
        def clazz = clazzService.getClazz('basis-group-sourced-id')

        then:
        clazz.sourcedId == 'basis-group-sourced-id'
        clazz.title == 'Basis group'
        clazz.classType == ClazzType.HOMEROOM
        clazz.course.sourcedId == 'level-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.size() == 1
        clazz.terms.first().sourcedId == 'T1SY20192020'
    }

    List<Clazz> getClazzes() {
        Clazz homeroom = new Clazz(
                'basis-group-sourced-id',
                'Basis group',
                ClazzType.HOMEROOM,
                GUIDRef.of(GUIDType.COURSE, 'level-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                [GUIDRef.of(GUIDType.ACADEMICSESSION, 'T1SY20192020')]
        )

        Clazz scheduled = new Clazz(
                'teaching-group-sourced-id',
                'Teaching group',
                ClazzType.SCHEDULED,
                GUIDRef.of(GUIDType.COURSE, 'subject-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                [GUIDRef.of(GUIDType.ACADEMICSESSION, 'T1SY20192020')]
        )

        return [homeroom, scheduled]
    }

    AcademicSession getTerm() {
        return new AcademicSession(
                'T1SY20192020',
                '1 termin 2019/2020',
                LocalDate.of(2019, 8, 1),
                LocalDate.of(2010, 12, 31),
                SessionType.TERM,
                Year.of(2020)
        )
    }
}