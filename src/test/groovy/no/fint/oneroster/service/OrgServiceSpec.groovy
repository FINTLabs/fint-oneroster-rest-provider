package no.fint.oneroster.service

import no.fint.oneroster.model.Clazz
import no.fint.oneroster.model.Enrollment
import no.fint.oneroster.model.GUIDRef
import no.fint.oneroster.model.Org
import no.fint.oneroster.model.User
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.model.vocab.GUIDType
import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.repository.OneRosterService
import spock.lang.Specification

class OrgServiceSpec extends Specification {

    OneRosterService oneRosterService = Mock {
        getAllOrgs() >> getOrgs()
    }

    EnrollmentService enrollmentService = Mock {
        getAllEnrollments() >> getEnrollments()
    }

    UserService userService = Mock {
        getAllStudents() >> [getStudent()]
        getAllTeachers() >> [getTeacher()]
    }

    ClazzService clazzService = Mock {
        getAllClazzes() >> [getBasisGroup(), getTeachingGroup()]
    }

    OrgService orgService = new OrgService(oneRosterService, enrollmentService, userService, clazzService)

    def "getAllOrgs returns a list of orgs"() {
        when:
        def orgs = orgService.getAllOrgs()

        then:
        orgs.size() == 2
        orgs.first().sourcedId == 'school-owner-sourced-id'
        orgs.first().name == 'School owner'
        orgs.first().type == OrgType.DISTRICT

        orgs.last().sourcedId == 'school-sourced-id'
        orgs.last().name == 'School'
        orgs.last().type == OrgType.SCHOOL
    }

    def "getOrg returns an org given valid sourcedId"() {
        when:
        def org = orgService.getOrg('school-owner-sourced-id')

        then:
        org.sourcedId == 'school-owner-sourced-id'
        org.name == 'School owner'
        org.type == OrgType.DISTRICT
    }

    def "getAllSchools returns a list of schools"() {
        when:
        def schools = orgService.getAllSchools()

        then:
        schools.size() == 1
    }

    def "getSchool returns a school given valid sourcedId"() {
        when:
        def school = orgService.getSchool('school-sourced-id')

        then:
        school.sourcedId == 'school-sourced-id'
        school.name == 'School'
        school.type == OrgType.SCHOOL
    }

    def "getClazzesForSchool returns clazzes given av valid school sourcedId"() {
        when:
        def clazzes = orgService.getClazzesForSchool('school-sourced-id')

        then:
        clazzes.size() == 2
    }

    def "getEnrollmentsForSchool returns enrollments given valid school sourcedId"() {
        when:
        def enrollments = orgService.getEnrollmentsForSchool('school-sourced-id')

        then:
        enrollments.size() == 2
    }

    def "getStudentsForSchool returns a list of students given valid school sourcedId"() {
        when:
        def students = orgService.getStudentsForSchool('school-sourced-id')

        then:
        students.size() == 1
    }

    def "getTeachersForSchool returns a list of teachers given valid school sourcedId"() {
        when:
        def teachers = orgService.getTeachersForSchool('school-sourced-id')

        then:
        teachers.size() == 1
    }

    List<Org> getOrgs() {
        Org schoolOwner = new Org(
                'school-owner-sourced-id',
                'School owner',
                OrgType.DISTRICT
        )

        Org school = new Org(
                'school-sourced-id',
                'School',
                OrgType.SCHOOL
        )

        return [schoolOwner, school]
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
}
