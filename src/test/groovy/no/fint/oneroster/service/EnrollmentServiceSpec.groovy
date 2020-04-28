package no.fint.oneroster.service

import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.repository.FintEducationService
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

class EnrollmentServiceSpec extends Specification {

    FintEducationService fintEducationService = Mock {
        getSchools() >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
        getStudents() >> [('/student-sourced-id'): FintObjectFactory.newStudent()]
        getStudentRelations() >> [('/student-relation-sourced-id'): FintObjectFactory.newStudentRelation()]
        getBasisGroups() >> [('/basis-group-sourced-id'): FintObjectFactory.newBasisGroup()]
        getTeachingGroups() >> [('/teaching-group-sourced-id'): FintObjectFactory.newTeachingGroup()]

        getSchools() >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
        getTeachers() >> [('/teacher-sourced-id'): FintObjectFactory.newTeacher()]
        getTeachingRelations() >> [('/teaching-relation-sourced-id'): FintObjectFactory.newTeachingRelation()]
        getBasisGroups() >> [('/basis-group-sourced-id'): FintObjectFactory.newBasisGroup()]
        getTeachingGroups() >> [('/teaching-group-sourced-id'): FintObjectFactory.newTeachingGroup()]
    }

    EnrollmentService enrollmentService = new EnrollmentService(fintEducationService, oneRosterRepository)

    def "getAllEnrollments returns a list of enrollments given valid orgId"() {
        when:
        def enrollments = enrollmentService.getAllEnrollments()

        then:
        enrollments.size() == 4

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

    def "getEnrollment returns an enrollment given valid orgId and sourcedId"() {
        when:
        def enrollment = enrollmentService.getEnrollment('student-relation-sourced-id_basis-group-sourced-id')

        then:
        enrollment.sourcedId == 'student-relation-sourced-id_basis-group-sourced-id'
        enrollment.role == RoleType.STUDENT
        enrollment.user.sourcedId == 'student-sourced-id'
        enrollment.clazz.sourcedId == 'basis-group-sourced-id'
        enrollment.school.sourcedId == 'school-sourced-id'
    }
}
