package no.fint.oneroster.service

import no.fint.oneroster.repository.FintRepository
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

class CourseServiceSpec extends Specification {

    FintRepository fintRepository = Mock {
        getLevels(_ as String) >> [('/level-sourced-id'): FintObjectFactory.newLevel()]
        getSubjects(_ as String) >> [('/subject-sourced-id'): FintObjectFactory.newSubject()]
        getOrganisationalElements(_ as String) >> [('/school-owner-sourced-id'): FintObjectFactory.newSchoolOwner()]
    }

    CourseService courseService = new CourseService(fintRepository)

    def "getAllCourses returns a list of courses given valid orgId"() {
        when:
        def courses = courseService.getAllCourses(_ as String)

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
        def course = courseService.getCourse(_ as String, 'level-sourced-id')

        then:
        course.sourcedId == 'level-sourced-id'
        course.title == 'Level'
        course.org.sourcedId == 'school-owner-sourced-id'
        course.courseCode == '/grep-level'
    }
}

