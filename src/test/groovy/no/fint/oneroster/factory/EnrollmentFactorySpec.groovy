package no.fint.oneroster.factory

import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.FintObjectFactory
import spock.lang.Specification

class EnrollmentFactorySpec extends Specification {

    def "student() returns enrollment object of type student"() {
        when:
        def studentEnrollment = EnrollmentFactory.student(FintObjectFactory.newStudentRelation(), FintObjectFactory.newStudent(), FintObjectFactory.newBasisGroup(), FintObjectFactory.newSchool())

        then:
        studentEnrollment.sourcedId == 'student-relation-sourced-id_basis-group-sourced-id'
        studentEnrollment.school.sourcedId == 'school-sourced-id'
        studentEnrollment.clazz.sourcedId == 'basis-group-sourced-id'
        studentEnrollment.user.sourcedId == 'student-sourced-id'
        studentEnrollment.role == RoleType.STUDENT
    }

    def "teacher() returns enrollment object of type teacher"() {
        when:
        def teacherEnrollment = EnrollmentFactory.teacher(FintObjectFactory.newTeachingRelation(), FintObjectFactory.newTeacher(), FintObjectFactory.newBasisGroup(), FintObjectFactory.newSchool())

        then:
        teacherEnrollment.sourcedId == 'teaching-relation-sourced-id_basis-group-sourced-id'
        teacherEnrollment.school.sourcedId == 'school-sourced-id'
        teacherEnrollment.clazz.sourcedId == 'basis-group-sourced-id'
        teacherEnrollment.user.sourcedId == 'teacher-sourced-id'
        teacherEnrollment.role == RoleType.TEACHER
    }
}