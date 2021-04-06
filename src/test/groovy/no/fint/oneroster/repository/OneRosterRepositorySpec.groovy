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

class OneRosterRepositorySpec extends Specification {

    FintRepository fintService = Stub() {
        getSchools() >> [FintObjectFactory.newSchool()]
        getSchoolById(_ as String) >> FintObjectFactory.newSchool()
        getPersonById(_ as String) >> FintObjectFactory.newPerson()
        getStudentById(_ as String) >> FintObjectFactory.newStudent()
        getStudentRelationById(_ as String) >> FintObjectFactory.newStudentRelation()
        getTeacherById(_ as String) >> FintObjectFactory.newTeacher()
        getTeachingRelationById(_ as String) >> FintObjectFactory.newTeachingRelation()
        getBasisGroupById(_ as String) >> FintObjectFactory.newBasisGroup()
        getTeachingGroupById(_ as String) >> FintObjectFactory.newTeachingGroup()
        getContactTeacherGroupById(_ as String) >> FintObjectFactory.newContactTeacherGroup()
        getLevelById(_ as String) >> FintObjectFactory.newLevel()
        getSubjectById(_ as String) >> FintObjectFactory.newSubject()
        getPersonnelById(_ as String) >> FintObjectFactory.newPersonnel()
    }

    OneRosterProperties oneRosterProperties = Stub() {
        getOrg() >> new OneRosterProperties.Org(
                sourcedId: 'school-owner-sourced-id',
                name: 'School owner',
                identifier: 'identifier'
        )

        isContactTeacherGroups() >> true
    }

    AcademicSessionService academicSessionService = Stub() {
        getAllTerms() >> getTerms()
    }

    ClazzFactory clazzFactory = new DefaultClazzFactory()
    UserFactory userFactory = new DefaultUserFactory()

    OneRosterRepository oneRosterService = new OneRosterRepository(oneRosterProperties, academicSessionService, clazzFactory, userFactory, fintService)

    def setup()  {
        oneRosterService.update()
    }

    def "getAllOrgs returns a list of orgs"() {
        when:
        def orgs = oneRosterService.getOrgs()

        then:
        orgs.size() == 2
        orgs.first().sourcedId == 'school-owner-sourced-id'
        orgs.first().name == 'School owner'
        orgs.first().type == OrgType.DISTRICT
        orgs.first().identifier == 'identifier'
        orgs.first().children.first().sourcedId == 'school-sourced-id'
    }

    def "getAllClazzes returns a list of clazzes"() {
        when:
        def clazzes = oneRosterService.getClazzes()

        then:
        clazzes.size() == 3
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
        def courses = oneRosterService.getCourses()

        then:
        courses.size() == 2
        courses.first().sourcedId == 'level-sourced-id'
        courses.first().title == 'Level'
        courses.first().org.sourcedId == 'school-owner-sourced-id'
    }

    def "getAllEnrollments returns a list of enrollments"() {
        when:
        def enrollments = oneRosterService.getEnrollments()

        then:
        enrollments.size() == 6
        enrollments.first().sourcedId == 'student-relation-sourced-id_basis-group-sourced-id'
        enrollments.first().role == RoleType.STUDENT
        enrollments.first().user.sourcedId == 'student-sourced-id'
        enrollments.first().clazz.sourcedId == 'basis-group-sourced-id'
        enrollments.first().school.sourcedId == 'school-sourced-id'
    }

    def "getAllUsers returns a list of users"() {
        when:
        def users = oneRosterService.getUsers()

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
