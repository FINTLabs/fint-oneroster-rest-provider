package no.fint.oneroster.factory.user

import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.FintObjectFactory
import spock.lang.Specification

class NinUserFactorySpec extends Specification {
    NinUserFactory ninUserFactory = new NinUserFactory()

    def "student() returns user object of type student"() {
        when:
        def student = ninUserFactory.student(FintObjectFactory.newStudent(), FintObjectFactory.newPerson(), [FintObjectFactory.newSchool()])

        then:
        student.sourcedId == 'student-sourced-id'
        student.username == 'username'
        student.enabledUser
        student.givenName == 'given-name'
        student.familyName == 'family-name'
        student.role == RoleType.STUDENT
        student.identifier == 'person-sourced-id'
        student.orgs.first().sourcedId == 'school-sourced-id'
    }

    def "teacher() returns user object of type teacher"() {
        when:
        def teacher = ninUserFactory.teacher(FintObjectFactory.newTeacher(), FintObjectFactory.newPersonnel(), FintObjectFactory.newPerson(), [FintObjectFactory.newSchool()])

        then:
        teacher.sourcedId == 'teacher-sourced-id'
        teacher.username == 'username'
        teacher.enabledUser
        teacher.givenName == 'given-name'
        teacher.familyName == 'family-name'
        teacher.role == RoleType.TEACHER
        teacher.identifier == 'person-sourced-id'
        teacher.orgs.first().sourcedId == 'school-sourced-id'
    }
}
