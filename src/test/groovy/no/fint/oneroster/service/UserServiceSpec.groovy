package no.fint.oneroster.service

import no.fint.oneroster.model.GUIDRef
import no.fint.oneroster.model.Org
import no.fint.oneroster.model.User
import no.fint.oneroster.model.vocab.GUIDType
import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.properties.OneRosterProperties
import no.fint.oneroster.repository.OneRosterService
import spock.lang.Specification

class UserServiceSpec extends Specification {

    OneRosterService oneRosterService = Mock {
        getAllUsers() >> getUsers()
    }

    OrgService orgService = Mock()

    UserService userService = new UserService(oneRosterService, orgService)

    def "getAllUsers returns a list of users"() {
        when:
        def users = userService.getAllUsers()

        then:
        users.size() == 2
        users.first().sourcedId == 'student-sourced-id'
        users.first().username == 'username'
        users.first().enabledUser
        users.first().givenName == 'given-name'
        users.first().familyName == 'family-name'
        users.first().role == RoleType.STUDENT
        users.first().orgs.first().sourcedId == 'school-sourced-id'

        users.last().sourcedId == 'teacher-sourced-id'
        users.last().username == 'username'
        users.last().enabledUser
        users.last().givenName == 'given-name'
        users.last().familyName == 'family-name'
        users.last().role == RoleType.TEACHER
        users.last().orgs.first().sourcedId == 'school-sourced-id'
    }

    def "getUser returns a user given sourcedId"() {
        when:
        def user = userService.getUser('student-sourced-id')

        then:
        user.sourcedId == 'student-sourced-id'
        user.username == 'username'
        user.enabledUser
        user.givenName == 'given-name'
        user.familyName == 'family-name'
        user.role == RoleType.STUDENT
        user.orgs.first().sourcedId == 'school-sourced-id'
    }

    def "getAllStudents returns a list of students"() {
        when:
        def students = userService.getAllStudents()

        then:
        students.size() == 1
    }

    def "getStudent returns a student given valid sourcedId"() {
        when:
        def student = userService.getStudent('student-sourced-id')

        then:
        student.sourcedId == 'student-sourced-id'
        student.username == 'username'
        student.enabledUser
        student.givenName == 'given-name'
        student.familyName == 'family-name'
        student.role == RoleType.STUDENT
        student.orgs.first().sourcedId == 'school-sourced-id'
    }

    def "getAllTeachers returns a list of teachers"() {
        when:
        def teachers = userService.getAllTeachers()

        then:
        teachers.size() == 1
    }

    def "getTeacher returns a teacher given valid sourcedId"() {
        when:
        def teacher = userService.getTeacher('teacher-sourced-id')

        then:
        teacher.sourcedId == 'teacher-sourced-id'
        teacher.username == 'username'
        teacher.enabledUser
        teacher.givenName == 'given-name'
        teacher.familyName == 'family-name'
        teacher.role == RoleType.TEACHER
        teacher.orgs.first().sourcedId == 'school-sourced-id'
    }

    def "getStudentsForSchool returns a list of students given valid school sourcedId"() {
        when:
        def students = userService.getStudentsForSchool('school-sourced-id')

        then:
        orgService.getSchool('school-sourced-id') >> getSchool()
        students.size() == 1
    }

    def "getTeachersForSchool returns a list of teachers given valid school sourcedId"() {
        when:
        def teachers = userService.getTeachersForSchool('school-sourced-id')

        then:
        orgService.getSchool('school-sourced-id') >> getSchool()
        teachers.size() == 1
    }

    

    List<User> getUsers() {
        User student = new User(
                'student-sourced-id',
                'username',
                true,
                'given-name',
                'family-name',
                RoleType.STUDENT,
                [GUIDRef.of(GUIDType.ORG, 'school-sourced-id')]
        )

        User teacher = new User(
                'teacher-sourced-id',
                'username',
                true,
                'given-name',
                'family-name',
                RoleType.TEACHER,
                [GUIDRef.of(GUIDType.ORG, 'school-sourced-id')]
        )

        return [student, teacher]
    }

    Org getSchool() {
        return new Org(
                'school-sourced-id',
                'School',
                OrgType.SCHOOL
        )
    }
}
