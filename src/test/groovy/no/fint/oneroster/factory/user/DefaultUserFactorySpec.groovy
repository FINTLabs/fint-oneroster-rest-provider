package no.fint.oneroster.factory.user

import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.FintObjectFactory
import spock.lang.Specification

class DefaultUserFactorySpec extends Specification {
    DefaultUserFactory defaultUserFactory = new DefaultUserFactory()

    def "student() returns user object of type student"() {
        when:
        def student = defaultUserFactory.student(FintObjectFactory.newStudent(), FintObjectFactory.newPerson(), [FintObjectFactory.newSchool()])

        then:
        student.sourcedId == 'student-sourced-id'
        student.username == 'username'
        student.enabledUser
        student.givenName == 'given-name'
        student.familyName == 'family-name'
        student.role == RoleType.STUDENT
        student.orgs.first().sourcedId == 'school-sourced-id'
    }

    def "teacher() returns user object of type teacher"() {
        when:
        def teacher = defaultUserFactory.teacher(FintObjectFactory.newTeacher(), FintObjectFactory.newPersonnel(), FintObjectFactory.newPerson(), [FintObjectFactory.newSchool()])

        then:
        teacher.sourcedId == 'teacher-sourced-id'
        teacher.username == 'username'
        teacher.enabledUser
        teacher.givenName == 'given-name'
        teacher.familyName == 'family-name'
        teacher.role == RoleType.TEACHER
        teacher.orgs.first().sourcedId == 'school-sourced-id'
    }
}
