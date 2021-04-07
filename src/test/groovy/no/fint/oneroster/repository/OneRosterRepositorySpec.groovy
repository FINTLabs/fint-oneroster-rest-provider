package no.fint.oneroster.repository

import no.fint.oneroster.factory.clazz.ClazzFactory
import no.fint.oneroster.factory.clazz.DefaultClazzFactory
import no.fint.oneroster.factory.user.DefaultUserFactory
import no.fint.oneroster.factory.user.UserFactory
import no.fint.oneroster.model.vocab.ClazzType
import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.model.vocab.RoleType
import no.fint.oneroster.properties.OneRosterProperties
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

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
        getTermById(_ as String) >> FintObjectFactory.newTerm()
        getSchoolYearById(_ as String) >> FintObjectFactory.newSchoolYear()
    }

    OneRosterProperties oneRosterProperties = Stub() {
        getOrg() >> new OneRosterProperties.Org(
                sourcedId: 'school-owner-sourced-id',
                name: 'School owner',
                identifier: 'identifier'
        )

        isContactTeacherGroups() >> true
    }

    ClazzFactory clazzFactory = new DefaultClazzFactory()
    UserFactory userFactory = new DefaultUserFactory()

    OneRosterRepository oneRosterRepository = new OneRosterRepository(oneRosterProperties, clazzFactory, userFactory, fintService)

    def setup()  {
        oneRosterRepository.update()
    }

    def "getAllOrgs returns a list of orgs"() {
        when:
        def orgs = oneRosterRepository.getOrgs()

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
        def clazzes = oneRosterRepository.getClazzes()

        then:
        clazzes.size() == 3
        clazzes.first().sourcedId == 'basis-group-sourced-id'
        clazzes.first().title == 'Basis group'
        clazzes.first().classType == ClazzType.HOMEROOM
        clazzes.first().course.sourcedId == 'level-sourced-id'
        clazzes.first().school.sourcedId == 'school-sourced-id'
        clazzes.first().terms.size() == 1
        clazzes.first().terms.first().sourcedId == 'term-sourced-id'
    }

    def "getAllCourses returns a list of courses"() {
        when:
        def courses = oneRosterRepository.getCourses()

        then:
        courses.size() == 2
        courses.first().sourcedId == 'level-sourced-id'
        courses.first().title == 'Level'
        courses.first().org.sourcedId == 'school-owner-sourced-id'
    }

    def "getAllEnrollments returns a list of enrollments"() {
        when:
        def enrollments = oneRosterRepository.getEnrollments()

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
        def users = oneRosterRepository.getUsers()

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
}
