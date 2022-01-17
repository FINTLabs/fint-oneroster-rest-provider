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

class ClazzServiceSpec extends Specification {

    OneRosterRepository oneRosterService = Mock()

    ClazzService clazzService = new ClazzService(oneRosterService)

    def "getAllClazzes returns a list of clazzes"() {
        when:
        def clazzes = clazzService.getAllClazzes()

        then:
        oneRosterService.getClazzes() >> [getBasisGroup(), getTeachingGroup()]
        clazzes.size() == 2
        clazzes.first().sourcedId == 'basis-group-sourced-id'
        clazzes.first().title == 'Basis group'
        clazzes.first().classType == ClazzType.HOMEROOM
        clazzes.first().course.sourcedId == 'level-sourced-id'
        clazzes.first().school.sourcedId == 'school-sourced-id'
        clazzes.first().terms.size() == 1
        clazzes.first().terms.first().sourcedId == 'T1SY20192020'

        clazzes.last().sourcedId == 'teaching-group-sourced-id'
        clazzes.last().title == 'Teaching group'
        clazzes.last().classType == ClazzType.SCHEDULED
        clazzes.last().course.sourcedId == 'subject-sourced-id'
        clazzes.last().school.sourcedId == 'school-sourced-id'
        clazzes.last().terms.size() == 1
        clazzes.last().terms.last().sourcedId == 'T1SY20192020'
    }

    def "getClazz returns a clazz given valid sourcedId"() {
        when:
        def clazz = clazzService.getClazz('basis-group-sourced-id')

        then:
        oneRosterService.getClazzById(_ as String) >> getBasisGroup()
        clazz.sourcedId == 'basis-group-sourced-id'
        clazz.title == 'Basis group'
        clazz.classType == ClazzType.HOMEROOM
        clazz.course.sourcedId == 'level-sourced-id'
        clazz.school.sourcedId == 'school-sourced-id'
        clazz.terms.size() == 1
        clazz.terms.first().sourcedId == 'T1SY20192020'
    }

    def "getStudentsForClazz returns a list of students given valid clazz sourcedId"() {
        when:
        def students = clazzService.getStudentsForClazz('basis-group-sourced-id')

        then:
        oneRosterService.getClazzById(_ as String) >> getBasisGroup()
        oneRosterService.getEnrollments() >> [getStudentEnrollment()]
        oneRosterService.getUserById(_ as String) >> getStudent()
        students.size() == 1
    }

    def "getTeachersForClazz returns a list of teachers given valid clazz sourcedId"() {
        when:
        def teachers = clazzService.getTeachersForClazz('teaching-group-sourced-id')

        then:
        oneRosterService.getClazzById(_ as String) >> getTeachingGroup()
        oneRosterService.getEnrollments() >> [getTeacherEnrollment()]
        oneRosterService.getTeacherById(_ as String) >> getTeacher()
        teachers.size() == 1
    }

    Enrollment getStudentEnrollment() {
        return new Enrollment(
                'student-relation-sourced-id_basis-group-sourced-id',
                GUIDRef.of(GUIDType.USER, 'student-sourced-id'),
                GUIDRef.of(GUIDType.CLASS, 'basis-group-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                RoleType.STUDENT
        )
    }

    Enrollment getTeacherEnrollment() {
        return new Enrollment(
                'teaching-relation-sourced-id_teaching-group-sourced-id',
                GUIDRef.of(GUIDType.USER, 'teacher-sourced-id'),
                GUIDRef.of(GUIDType.CLASS, 'teaching-group-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                RoleType.TEACHER
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
}