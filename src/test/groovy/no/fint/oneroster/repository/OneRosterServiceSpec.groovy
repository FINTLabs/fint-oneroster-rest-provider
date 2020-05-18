package no.fint.oneroster.repository

import no.fint.oneroster.factory.clazz.ClazzFactory
import no.fint.oneroster.factory.clazz.DefaultClazzFactory
import no.fint.oneroster.factory.user.DefaultUserFactory
import no.fint.oneroster.factory.user.UserFactory
import no.fint.oneroster.model.AcademicSession
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.model.vocab.SessionType
import no.fint.oneroster.properties.OneRosterProperties
import no.fint.oneroster.service.AcademicSessionService
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

import java.time.LocalDate
import java.time.Year

class OneRosterServiceSpec extends Specification {

    FintEducationService fintEducationService = Mock {
        getSchools() >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
        getPersons() >> [('/person-sourced-id'): FintObjectFactory.newPerson()]
        getStudents() >> [('/student-sourced-id'): FintObjectFactory.newStudent()]
        getStudentRelations() >> [('/student-relation-sourced-id'): FintObjectFactory.newStudentRelation()]
        getTeachers() >> [('/teacher-sourced-id'): FintObjectFactory.newTeacher()]
        getTeachingRelations() >> [('/teaching-relation-sourced-id'): FintObjectFactory.newTeachingRelation()]
        getBasisGroups() >> [('/basis-group-sourced-id'): FintObjectFactory.newBasisGroup()]
        getTeachingGroups() >> [('/teaching-group-sourced-id'): FintObjectFactory.newTeachingGroup()]
        getLevels() >> [('/level-sourced-id'): FintObjectFactory.newLevel()]
        getSubjects() >> [('/subject-sourced-id'): FintObjectFactory.newSubject()]
    }

    FintAdministrationService fintAdministrationService = Mock {
        getPersons() >> [('/person-sourced-id'): FintObjectFactory.newPerson()]
        getPersonnel() >> [('/personnel-resource-sourced-id'): FintObjectFactory.newPersonnel()]
    }

    OneRosterProperties oneRosterProperties = Mock {
        getOrg() >> new OneRosterProperties.Org(
                sourcedId: 'school-owner-sourced-id',
                name: 'Org',
                identifier: 'identifier'
        )
    }

    AcademicSessionService academicSessionService = Mock {
        getAllTerms() >> getTerms()
    }

    ClazzFactory clazzFactory = new DefaultClazzFactory()
    UserFactory userFactory = new DefaultUserFactory()

    OneRosterService oneRosterService = new OneRosterService(fintEducationService, fintAdministrationService, oneRosterProperties, academicSessionService, clazzFactory, userFactory)

    def "getAllOrgs returns a list of orgs"() {
        when:
        def orgs = oneRosterService.getAllOrgs()

        then:
        orgs.size() == 2
        orgs.first().sourcedId == 'school-sourced-id'
        orgs.first().name == 'School'
        orgs.first().type == OrgType.SCHOOL
        orgs.first().identifier == 'identifier'
        orgs.first().parent.sourcedId == 'school-owner-sourced-id'
    }

    def "getAllClazzes returns a list of clazzes"() {
        when:
        def clazzes = oneRosterService.getAllClazzes()

        then:
        clazzes.size() == 2
        clazzes.first().sourcedId == 'basis-group-sourced-id'
        clazzes.first().title == 'Basis group'
        clazzes.first().classType == ClazzType.HOMEROOM
        clazzes.first().course.sourcedId == 'level-sourced-id'
        clazzes.first().school.sourcedId == 'school-sourced-id'
        clazzes.first().terms.size() == 2
        clazzes.first().terms.first().sourcedId == 'T1SY20192020'
    }

    def "getAllCourses returns a list of courses"() {
        when:
        def courses = oneRosterService.getAllCourses()

        then:
        courses.size() == 2
        courses.first().sourcedId == 'level-sourced-id'
        courses.first().title == 'Level'
        courses.first().org.sourcedId == 'school-owner-sourced-id'
    }

    def "getAllEnrollments returns a list of enrollments"() {
        when:
        def enrollments = oneRosterService.getAllEnrollments()

        then:
        enrollments.size() == 4
        enrollments.first().sourcedId == 'student-relation-sourced-id_basis-group-sourced-id'
        enrollments.first().role == RoleType.STUDENT
        enrollments.first().user.sourcedId == 'student-sourced-id'
        enrollments.first().clazz.sourcedId == 'basis-group-sourced-id'
        enrollments.first().school.sourcedId == 'school-sourced-id'
    }

    def "getAllUsers returns a list of users"() {
        when:
        def users = oneRosterService.getAllUsers()

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
        users.first().email == 'email'
        users.first().orgs.first().sourcedId == 'school-sourced-id'
    }

    List<AcademicSession> getTerms() {
        AcademicSession firstTerm = new AcademicSession(
                'T1SY20192020',
                '1 termin 2019/2020',
                LocalDate.of(2019, 8, 1),
                LocalDate.of(2010, 12, 31),
                SessionType.TERM,
                Year.of(2020)
        )

        AcademicSession secondTerm = new AcademicSession(
                'T2SY20192020',
                '2 termin 2019/2020',
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2020, 7, 31),
                SessionType.TERM,
                Year.of(2020)
        )

        return [firstTerm, secondTerm]
    }
}
