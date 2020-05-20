package no.fint.oneroster.factory.user

import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

class ROGFKUserFactorySpec extends Specification {
    ROGFKUserFactory rogfkUserFactory = new ROGFKUserFactory()

    def "student() returns user object of type student"() {
        when:
        def student = rogfkUserFactory.student(FintObjectFactory.newStudent(), FintObjectFactory.newPerson(), [FintObjectFactory.newSchool()])

        then:
        student.sourcedId == 'student-sourced-id'
        student.username == 'username'
        student.enabledUser
        student.givenName == 'given-name'
        student.familyName == 'family-name'
        student.role == RoleType.STUDENT
        student.identifier == 'identifier'
        student.orgs.first().sourcedId == 'school-sourced-id'
    }

    def "teacher() returns user object of type teacher"() {
        when:
        def teacher = rogfkUserFactory.teacher(FintObjectFactory.newTeacher(), FintObjectFactory.newPersonnel(), FintObjectFactory.newPerson(), [FintObjectFactory.newSchool()])

        then:
        teacher.sourcedId == 'teacher-sourced-id'
        teacher.username == 'username'
        teacher.enabledUser
        teacher.givenName == 'given-name'
        teacher.familyName == 'family-name'
        teacher.role == RoleType.TEACHER
        teacher.identifier == 'identifier'
        teacher.orgs.first().sourcedId == 'school-sourced-id'
    }
}
