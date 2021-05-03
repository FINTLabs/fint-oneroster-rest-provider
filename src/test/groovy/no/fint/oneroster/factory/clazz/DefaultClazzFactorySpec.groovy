package no.fint.oneroster.factory.clazz

import no.fint.oneroster.model.AcademicSession
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.FintObjectFactory
import no.fint.oneroster.model.vocab.SessionType
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
        clazz.terms.first().sourcedId == 'term-sourced-id'
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
        clazz.terms.first().sourcedId == 'term-sourced-id'
    }

    def getTerm() {
        return new AcademicSession(
                'term-sourced-id',
                '1 termin 2019/2020',
                LocalDate.of(2019, 8, 1),
                LocalDate.of(2010, 12, 31),
                SessionType.TERM,
                Year.of(2020)
        )
    }
}