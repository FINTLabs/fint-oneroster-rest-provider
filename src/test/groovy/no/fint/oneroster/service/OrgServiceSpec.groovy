package no.fint.oneroster.service

import no.fint.oneroster.model.AcademicSession
import no.fint.oneroster.model.Clazz
import no.fint.oneroster.model.Enrollment
import no.fint.oneroster.model.GUIDRef
import no.fint.oneroster.model.Org
import no.fint.oneroster.model.User
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.model.vocab.GUIDType
import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.model.vocab.StatusType
import no.fint.oneroster.repository.OneRosterRepository
import spock.lang.Specification

import java.time.LocalDate
import java.time.Year

class OrgServiceSpec extends Specification {

    OneRosterRepository oneRosterService = Mock()

    AcademicSessionService academicSessionService = Mock {
        getAllTerms() >> getTerms()
    }

    OrgService orgService = new OrgService(oneRosterService, academicSessionService)

    def "getAllOrgs returns a list of orgs"() {
        when:
        def orgs = orgService.getAllOrgs()

        then:
        oneRosterService.getOrgs() >> [getSchoolOwner(), getSchool()]
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
        oneRosterService.getOrgById(_ as String) >> getSchoolOwner()
        org.sourcedId == 'school-owner-sourced-id'
        org.name == 'School owner'
        org.type == OrgType.DISTRICT
    }

    def "getAllSchools returns a list of schools"() {
        when:
        def schools = orgService.getAllSchools()

        then:
        oneRosterService.getOrgs() >> [getSchoolOwner(), getSchool()]
        schools.size() == 1
    }

    def "getSchool returns a school given valid sourcedId"() {
        when:
        def school = orgService.getSchool('school-sourced-id')

        then:
        oneRosterService.getOrgById(_ as String) >> getSchool()
        school.sourcedId == 'school-sourced-id'
        school.name == 'School'
        school.type == OrgType.SCHOOL
    }

    def "getClazzesForSchool returns clazzes given av valid school sourcedId"() {
        when:
        def clazzes = orgService.getClazzesForSchool('school-sourced-id')

        then:
        oneRosterService.getOrgById(_ as String) >> getSchool()
        oneRosterService.getClazzes() >> [getBasisGroup(), getTeachingGroup()]
        clazzes.size() == 2
    }

    def "getEnrollmentsForSchool returns enrollments given valid school sourcedId"() {
        when:
        def enrollments = orgService.getEnrollmentsForSchool('school-sourced-id')

        then:
        oneRosterService.getOrgById(_ as String) >> getSchool()
        oneRosterService.getEnrollments() >> [getStudentEnrollment(), getTeacherEnrollment()]
        enrollments.size() == 2
    }

    def "getStudentsForSchool returns a list of students given valid school sourcedId"() {
        when:
        def students = orgService.getStudentsForSchool('school-sourced-id')

        then:
        oneRosterService.getOrgById(_ as String) >> getSchool()
        oneRosterService.getUsers() >> [getStudent(), getTeacher()]
        students.size() == 1
    }

    def "getTeachersForSchool returns a list of teachers given valid school sourcedId"() {
        when:
        def teachers = orgService.getTeachersForSchool('school-sourced-id')

        then:
        oneRosterService.getOrgById(_ as String) >> getSchool()
        oneRosterService.getUsers() >> [getStudent(), getTeacher()]
        teachers.size() == 1
    }

    def "getEnrollmentsForClazzInSchool returns a list of enrollments given valid school sourcedId and clazz sourcedId"() {
        when:
        def enrollments = orgService.getEnrollmentsForClazzInSchool('school-sourced-id', 'basis-group-sourced-id')

        then:
        oneRosterService.getOrgById(_ as String) >> getSchool()
        oneRosterService.getClazzById(_ as String) >> getBasisGroup()
        oneRosterService.getEnrollments() >> [getStudentEnrollment(), getTeacherEnrollment()]
        enrollments.size() == 1
    }

    def "getTermsForSchool returns a list of terms given valid school sourcedId"() {
        when:
        def terms = orgService.getTermsForSchool('school-sourced-id')

        then:
        oneRosterService.getOrgById(_ as String) >> getSchool()
        terms.size() == 2
    }

    Org getSchoolOwner() {
        return new Org(
                'school-owner-sourced-id',
                'School owner',
                OrgType.DISTRICT
        )
    }

    Org getSchool() {
        return new Org(
                'school-sourced-id',
                'School',
                OrgType.SCHOOL
        )
    }

    Enrollment getStudentEnrollment() {
        return new Enrollment(
                'student-relation-sourced-id_basis-group-sourced-id',
                StatusType.ACTIVE,
                GUIDRef.of(GUIDType.USER, 'student-sourced-id'),
                GUIDRef.of(GUIDType.CLASS, 'basis-group-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                RoleType.STUDENT
        )
    }

    Enrollment getTeacherEnrollment() {
        return new Enrollment(
                'teaching-relation-sourced-id_teaching-group-sourced-id',
                StatusType.ACTIVE,
                GUIDRef.of(GUIDType.USER, 'teacher-sourced-id'),
                GUIDRef.of(GUIDType.CLASS, 'teaching-group-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                RoleType.TEACHER
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

    Clazz getBasisGroup() {
        return new Clazz(
                'basis-group-sourced-id',
                StatusType.ACTIVE,
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
                StatusType.ACTIVE,
                'Teaching group',
                ClazzType.SCHEDULED,
                GUIDRef.of(GUIDType.COURSE, 'subject-sourced-id'),
                GUIDRef.of(GUIDType.ORG, 'school-sourced-id'),
                [GUIDRef.of(GUIDType.ACADEMICSESSION, 'T1SY20192020')]
        )
    }

    List<AcademicSession> getTerms() {
        AcademicSession firstTerm = new AcademicSession(
                'T1SY20192020',
                '1. termin 2019/2020',
                LocalDate.parse('2019-08-01'),
                LocalDate.parse('2019-12-31'),
                SessionType.TERM,
                Year.of(2020))

        AcademicSession secondTerm = new AcademicSession(
                'T2SY20192020',
                '2. termin 2019/2020',
                LocalDate.parse('2020-01-01'),
                LocalDate.parse('2020-07-31'),
                SessionType.TERM,
                Year.of(2020))

        return [firstTerm, secondTerm]
    }
}
