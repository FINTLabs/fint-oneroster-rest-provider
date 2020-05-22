package no.fint.oneroster.service

import no.fint.oneroster.model.Enrollment
import no.fint.oneroster.model.GUIDRef
import no.fint.oneroster.model.Org
import no.fint.oneroster.model.vocab.GUIDType
import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.repository.OneRosterService
import spock.lang.Specification

class EnrollmentServiceSpec extends Specification {

    OneRosterService oneRosterService = Mock {
        getAllEnrollments() >> getEnrollments()
    }

    OrgService orgService = Mock()

    EnrollmentService enrollmentService = new EnrollmentService(oneRosterService, orgService)

    def "getAllEnrollments returns a list of enrollments"() {
        when:
        def enrollments = enrollmentService.getAllEnrollments()

        then:
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
        enrollment.sourcedId == 'student-relation-sourced-id_basis-group-sourced-id'
        enrollment.role == RoleType.STUDENT
        enrollment.user.sourcedId == 'student-sourced-id'
        enrollment.clazz.sourcedId == 'basis-group-sourced-id'
        enrollment.school.sourcedId == 'school-sourced-id'
    }

    def "getEnrollmentsForSchool returns enrollments given valid school sourcedId"() {
        when:
        def enrollments = enrollmentService.getEnrollmentsForSchool('school-sourced-id')

        then:
        orgService.getSchool('school-sourced-id') >> getSchool()
        enrollments.size() == 2
    }

    List<Enrollment> getEnrollments() {
        Enrollment student = new Enrollment(
                'student-relation-sourced-id_basis-group-sourced-id',
                GUIDRef.of(GUIDType.USER, 'student-sourced-id'),
                GUIDRef.of(GUIDType.CLASS, 'basis-group-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                RoleType.STUDENT
        )

        Enrollment teacher = new Enrollment(
                'teaching-relation-sourced-id_teaching-group-sourced-id',
                GUIDRef.of(GUIDType.USER, 'teacher-sourced-id'),
                GUIDRef.of(GUIDType.CLASS, 'teaching-group-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                RoleType.TEACHER
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
