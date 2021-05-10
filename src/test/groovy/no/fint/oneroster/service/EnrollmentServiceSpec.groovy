package no.fint.oneroster.service

import no.fint.oneroster.model.Enrollment
import no.fint.oneroster.model.GUIDRef
import no.fint.oneroster.model.vocab.GUIDType
import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.model.vocab.StatusType
import no.fint.oneroster.repository.OneRosterRepository
import spock.lang.Specification

class EnrollmentServiceSpec extends Specification {

    OneRosterRepository oneRosterService = Mock()

    EnrollmentService enrollmentService = new EnrollmentService(oneRosterService)

    def "getAllEnrollments returns a list of enrollments"() {
        when:
        def enrollments = enrollmentService.getAllEnrollments()

        then:
        oneRosterService.getEnrollments() >> [getStudentEnrollment(), getTeacherEnrollment()]
        enrollments.size() == 2
        enrollments.first().sourcedId == 'student-relation-sourced-id_basis-group-sourced-id'
        enrollments.first().role == RoleType.STUDENT
        enrollments.first().user.sourcedId == 'student-sourced-id'
        enrollments.first().clazz.sourcedId == 'basis-group-sourced-id'
        enrollments.first().school.sourcedId == 'school-sourced-id'

        enrollments.last().sourcedId == 'teaching-relation-sourced-id_teaching-group-sourced-id'
        enrollments.last().role == RoleType.TEACHER
        enrollments.last().user.sourcedId == 'teacher-sourced-id'
        enrollments.last().clazz.sourcedId == 'teaching-group-sourced-id'
        enrollments.last().school.sourcedId == 'school-sourced-id'
    }

    def "getEnrollment returns an enrollment given valid sourcedId"() {
        when:
        def enrollment = enrollmentService.getEnrollment('student-relation-sourced-id_basis-group-sourced-id')

        then:
        oneRosterService.getEnrollmentById(_ as String) >> getStudentEnrollment()
        enrollment.sourcedId == 'student-relation-sourced-id_basis-group-sourced-id'
        enrollment.role == RoleType.STUDENT
        enrollment.user.sourcedId == 'student-sourced-id'
        enrollment.clazz.sourcedId == 'basis-group-sourced-id'
        enrollment.school.sourcedId == 'school-sourced-id'
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
}
