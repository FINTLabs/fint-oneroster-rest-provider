package no.fint.oneroster.service

import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.repository.FintRepository
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

class UserServiceSpec extends Specification {

    FintRepository fintRepository = Mock {
        1 * getSchools() >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
        1 * getPersons() >> [('/person-sourced-id'): FintObjectFactory.newPerson()]
        1 * getStudents() >> [('/student-sourced-id'): FintObjectFactory.newStudent()]
        1 * getStudentRelations() >> [('/student-relation-sourced-id'): FintObjectFactory.newStudentRelation()]

        1 * getSchools() >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
        1 * getPersons() >> [('/person-sourced-id'): FintObjectFactory.newPerson()]
        1 * getPersonnelResources() >> [('/personnel-resource-sourced-id'): FintObjectFactory.newPersonnelResource()]
        1 * getTeachers() >> [('/teacher-sourced-id'): FintObjectFactory.newTeacher()]
        1 * getTeachingRelations() >> [('/teaching-relation-sourced-id'): FintObjectFactory.newTeachingRelation()]
    }

    UserService userService = new UserService(fintRepository)

    def "getAllUsers returns a list of users given valid orgId"() {
        when:
        def users = userService.getAllUsers()

        then:
        users.size() == 2

        users.first().sourcedId == 'student-sourced-id'
        users.first().username == 'username'
        users.first().userIds.first().type == 'Feide'
        users.first().userIds.first().identifier == 'feide'
        users.first().enabledUser
        users.first().givenName == 'given-name'
        users.first().middleName == 'middle-name'
        users.first().familyName == 'family-name'
        users.first().role == RoleType.STUDENT
        users.first().identifier == 'identifier'
        users.first().email == 'email'
        users.first().sms == 'sms'
        users.first().phone == 'phone'
        users.first().orgs.first().sourcedId == 'school-sourced-id'

        users.last().sourcedId == 'teacher-sourced-id'
        users.last().username == 'username'
        users.last().userIds.first().type == 'Feide'
        users.last().userIds.first().identifier == 'feide'
        users.last().enabledUser
        users.last().givenName == 'given-name'
        users.last().middleName == 'middle-name'
        users.last().familyName == 'family-name'
        users.last().role == RoleType.TEACHER
        users.last().identifier == 'identifier'
        users.last().email == 'email'
        users.last().sms == 'sms'
        users.last().phone == 'phone'
        users.last().orgs.first().sourcedId == 'school-sourced-id'
    }

    def "getUser returns a user given valid orgId and sourcedId"() {
        when:
        def user = userService.getUser('student-sourced-id')

        then:
        user.sourcedId == 'student-sourced-id'
        user.username == 'username'
        user.userIds.first().type == 'Feide'
        user.userIds.first().identifier == 'feide'
        user.enabledUser
        user.givenName == 'given-name'
        user.middleName == 'middle-name'
        user.familyName == 'family-name'
        user.role == RoleType.STUDENT
        user.identifier == 'identifier'
        user.email == 'email'
        user.sms == 'sms'
        user.phone == 'phone'
        user.orgs.first().sourcedId == 'school-sourced-id'
    }

    def "getAllStudents returns a list of users given valid orgId"() {
        when:
        def students = userService.getAllStudents()

        then:
        students.size() == 1
    }

    def "getStudent returns a student given valid orgId and sourcedId"() {
        when:
        def student = userService.getStudent('student-sourced-id')

        then:
        student.sourcedId == 'student-sourced-id'
        student.username == 'username'
        student.userIds.first().type == 'Feide'
        student.userIds.first().identifier == 'feide'
        student.enabledUser
        student.givenName == 'given-name'
        student.middleName == 'middle-name'
        student.familyName == 'family-name'
        student.role == RoleType.STUDENT
        student.identifier == 'identifier'
        student.email == 'email'
        student.sms == 'sms'
        student.phone == 'phone'
        student.orgs.first().sourcedId == 'school-sourced-id'
    }

    def "getAllTeachers returns a list of users given valid orgId"() {
        when:
        def teachers = userService.getAllTeachers()

        then:
        teachers.size() == 1
    }

    def "getTeacher returns a teacher given valid orgId and sourcedId"() {
        when:
        def teacher = userService.getTeacher('teacher-sourced-id')

        then:
        teacher.sourcedId == 'teacher-sourced-id'
        teacher.username == 'username'
        teacher.userIds.first().type == 'Feide'
        teacher.userIds.first().identifier == 'feide'
        teacher.enabledUser
        teacher.givenName == 'given-name'
        teacher.middleName == 'middle-name'
        teacher.familyName == 'family-name'
        teacher.role == RoleType.TEACHER
        teacher.identifier == 'identifier'
        teacher.email == 'email'
        teacher.sms == 'sms'
        teacher.phone == 'phone'
        teacher.orgs.first().sourcedId == 'school-sourced-id'
    }
}
