package no.fint.oneroster.factory.user

import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.FintObjectFactory
import no.fint.oneroster.properties.OneRosterProperties
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

    def "student() returns user object of type student with parents"() {
        when:
        def student = defaultUserFactory.student(FintObjectFactory.newStudent(), FintObjectFactory.newPerson(), [FintObjectFactory.newSchool()], ['parent-sourced-id'].toSet())

        then:
        student.sourcedId == 'student-sourced-id'
        student.username == 'username'
        student.enabledUser
        student.givenName == 'given-name'
        student.familyName == 'family-name'
        student.role == RoleType.STUDENT
        student.orgs.first().sourcedId == 'school-sourced-id'
        student.agents.first().sourcedId == 'parent-sourced-id'
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

    def "parent() returns user object of type parent"() {
        when:
        def teacher = defaultUserFactory.parent(FintObjectFactory.newPerson(), ['child-sourced-id'].toSet(), new OneRosterProperties.Org(sourcedId: 'org-sourced-id') )

        then:
        teacher.sourcedId == 'person-sourced-id'
        teacher.username == ''
        teacher.enabledUser
        teacher.givenName == 'given-name'
        teacher.familyName == 'family-name'
        teacher.role == RoleType.PARENT
        teacher.orgs.first().sourcedId == 'org-sourced-id'
        teacher.agents.first().sourcedId == 'child-sourced-id'
    }
}