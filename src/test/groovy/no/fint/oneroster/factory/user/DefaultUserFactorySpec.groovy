package no.fint.oneroster.factory.user

import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.FintObjectFactory
import no.fint.oneroster.properties.OneRosterProperties
import no.fint.oneroster.util.PersonUtil
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
        def student = defaultUserFactory.student(FintObjectFactory.newStudent(), FintObjectFactory.newPerson(), [FintObjectFactory.newSchool()], [FintObjectFactory.newPerson()])

        then:
        student.sourcedId == 'student-sourced-id'
        student.username == 'username'
        student.enabledUser
        student.givenName == 'given-name'
        student.familyName == 'family-name'
        student.role == RoleType.STUDENT
        student.orgs.first().sourcedId == 'school-sourced-id'
        student.agents.first().sourcedId == PersonUtil.maskNin('person-sourced-id')
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
        def parent = defaultUserFactory.parent(FintObjectFactory.newPerson(), FintObjectFactory.newStudent(), new OneRosterProperties.Org(sourcedId: 'org-sourced-id') )

        then:
        parent.sourcedId == 'person-sourced-id'
        parent.username == ''
        parent.enabledUser
        parent.givenName == 'given-name'
        parent.familyName == 'family-name'
        parent.role == RoleType.PARENT
        parent.orgs.first().sourcedId == 'org-sourced-id'
        parent.agents.first().sourcedId == 'student-sourced-id'
    }
}