package no.fint.oneroster.service

import no.fint.oneroster.properties.OneRosterProperties
import no.fint.oneroster.repository.FintEducationService
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

class CourseServiceSpec extends Specification {

    FintEducationService fintEducationService = Mock {
        getLevels() >> [('/level-sourced-id'): FintObjectFactory.newLevel()]
        getSubjects() >> [('/subject-sourced-id'): FintObjectFactory.newSubject()]
    }

    OneRosterProperties organisationProperties = Mock {
        getOrg() >> new OneRosterProperties.Org(
                sourcedId: 'school-owner-sourced-id',
                name: 'Org',
                identifier: '0123456789'
        )
    }

    CourseService courseService = new CourseService(fintEducationService, organisationProperties)

    def "getAllCourses returns a list of courses given valid orgId"() {
        when:
        def courses = courseService.getAllCourses()

        then:
        courses.size() == 2

        courses.first().sourcedId == 'level-sourced-id'
        courses.first().title == 'Level'
        courses.first().org.sourcedId == 'school-owner-sourced-id'
        courses.first().courseCode == '/grep-level'

        courses.last().sourcedId == 'subject-sourced-id'
        courses.last().title == 'Subject'
        courses.last().org.sourcedId == 'school-owner-sourced-id'
        courses.last().courseCode == '/grep-subject'
    }

    def "getCourse returns a course given valid orgId and sourcedId"() {
        when:
        def course = courseService.getCourse('level-sourced-id')

        then:
        course.sourcedId == 'level-sourced-id'
        course.title == 'Level'
        course.org.sourcedId == 'school-owner-sourced-id'
        course.courseCode == '/grep-level'
    }
}

