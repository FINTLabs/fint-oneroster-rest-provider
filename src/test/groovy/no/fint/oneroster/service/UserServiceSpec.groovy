package no.fint.oneroster.service

import no.fint.oneroster.model.Clazz
import no.fint.oneroster.model.Enrollment
import no.fint.oneroster.model.GUIDRef
import no.fint.oneroster.model.User
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.model.vocab.GUIDType
import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.model.vocab.StatusType
import no.fint.oneroster.repository.OneRosterRepository
import spock.lang.Specification

class UserServiceSpec extends Specification {

    OneRosterRepository oneRosterService = Mock()

    UserService userService = new UserService(oneRosterService)

    def "getAllUsers returns a list of users"() {
        when:
        def users = userService.getAllUsers()

        then:
        oneRosterService.getUsers() >> [getStudent(), getTeacher()]
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
        oneRosterService.getUserById(_ as String) >> getStudent()
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
        oneRosterService.getUsers() >> [getStudent(), getTeacher()]
        students.size() == 1
    }

    def "getStudent returns a student given valid sourcedId"() {
        when:
        def student = userService.getStudent('student-sourced-id')

        then:
        oneRosterService.getUserById(_ as String) >> getStudent()
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
        oneRosterService.getUsers() >> [getStudent(), getTeacher()]
        teachers.size() == 1
    }

    def "getTeacher returns a teacher given valid sourcedId"() {
        when:
        def teacher = userService.getTeacher('teacher-sourced-id')

        then:
        oneRosterService.getUserById(_ as String) >> getTeacher()
        teacher.sourcedId == 'teacher-sourced-id'
        teacher.username == 'username'
        teacher.enabledUser
        teacher.givenName == 'given-name'
        teacher.familyName == 'family-name'
        teacher.role == RoleType.TEACHER
        teacher.orgs.first().sourcedId == 'school-sourced-id'
    }

    def "getClazzesForStudent returns clazzes given av valid student sourcedId"() {
        when:
        def clazzes = userService.getClazzesForStudent('student-sourced-id')

        then:
        oneRosterService.getUserById(_ as String) >> getStudent()
        oneRosterService.getEnrollments() >> [getStudentEnrollment()]
        oneRosterService.getClazzById(_ as String) >> getBasisGroup()
        clazzes.size() == 1
    }

    def "getClazzesForTeacher returns clazzes given av valid teacher sourcedId"() {
        when:
        def clazzes = userService.getClazzesForTeacher('teacher-sourced-id')

        then:
        oneRosterService.getUserById(_ as String) >> getTeacher()
        oneRosterService.getEnrollments() >> [getTeacherEnrollment()]
        oneRosterService.getClazzById(_ as String) >> getTeachingGroup()
        clazzes.size() == 1
    }

    User getStudent() {
        return new User(
                'student-sourced-id',
                'username',
                true,
                'given-name',
                'family-name',
                RoleType.STUDENT,
                [GUIDRef.of(GUIDType.ORG, 'school-sourced-id')]
        )
    }

    User getTeacher() {
        return new User(
                'teacher-sourced-id',
                'username',
                true,
                'given-name',
                'family-name',
                RoleType.TEACHER,
                [GUIDRef.of(GUIDType.ORG, 'school-sourced-id')]
        )
    }

    Clazz getBasisGroup() {
        return new Clazz(
                'basis-group-sourced-id',
                'Basis group',
                ClazzType.HOMEROOM,
                GUIDRef.of(GUIDType.COURSE, 'level-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                [GUIDRef.of(GUIDType.ACADEMICSESSION, 'T1SY20192020')]
        )
    }

    Clazz getTeachingGroup() {
        return new Clazz(
                'teaching-group-sourced-id',
                'Teaching group',
                ClazzType.SCHEDULED,
                GUIDRef.of(GUIDType.COURSE, 'subject-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                [GUIDRef.of(GUIDType.ACADEMICSESSION, 'T1SY20192020')]
        )
    }

    Enrollment getStudentEnrollment()
    {
        return new Enrollment(
                'student-relation-sourced-id_basis-group-sourced-id',
                GUIDRef.of(GUIDType.USER, 'student-sourced-id'),
                GUIDRef.of(GUIDType.CLASS, 'basis-group-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                RoleType.STUDENT
        )
    }

    Enrollment getTeacherEnrollment()
    {
        return new Enrollment(
                'teaching-relation-sourced-id_teaching-group-sourced-id',
                GUIDRef.of(GUIDType.USER, 'teacher-sourced-id'),
                GUIDRef.of(GUIDType.CLASS, 'teaching-group-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                RoleType.TEACHER
        )
    }
}
