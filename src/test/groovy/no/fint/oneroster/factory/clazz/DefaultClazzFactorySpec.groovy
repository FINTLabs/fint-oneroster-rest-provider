package no.fint.oneroster.factory.clazz

import no.fint.oneroster.model.AcademicSession
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

import java.time.LocalDate
import java.time.Year

class DefaultClazzFactorySpec extends Specification {
    DefaultClazzFactory defaultClazzFactory = new DefaultClazzFactory()

    def "basisGroup() returns class object of type homeroom"() {
        when:
        def clazz = defaultClazzFactory.basisGroup(FintObjectFactory.newBasisGroup(), FintObjectFactory.newLevel(), FintObjectFactory.newSchool(), [getTerm()])

        then:
        clazz.sourcedId == 'basis-group-sourced-id'
        clazz.title == 'Basis group'
        clazz.classType == ClazzType.HOMEROOM
        clazz.course.sourcedId == 'level-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.first().sourcedId == 'T1SY20192020'
    }

    def "teachingGroup() returns class object of type scheduled"() {
        when:
        def clazz = defaultClazzFactory.teachingGroup(FintObjectFactory.newTeachingGroup(), FintObjectFactory.newSubject(), FintObjectFactory.newSchool(), [getTerm()])

        then:
        clazz.sourcedId == 'teaching-group-sourced-id'
        clazz.title == 'Teaching group'
        clazz.classType == ClazzType.SCHEDULED
        clazz.course.sourcedId == 'subject-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.first().sourcedId == 'T1SY20192020'
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