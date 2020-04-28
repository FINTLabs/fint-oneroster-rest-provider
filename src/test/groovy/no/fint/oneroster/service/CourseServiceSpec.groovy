package no.fint.oneroster.service

import no.fint.oneroster.model.Course
import no.fint.oneroster.model.GUIDRef
import no.fint.oneroster.model.vocab.GUIDType
import no.fint.oneroster.repository.OneRosterService
import spock.lang.Specification

class CourseServiceSpec extends Specification {

    OneRosterService oneRosterService = Mock {
        getAllCourses() >> getCourses()
    }

    CourseService courseService = new CourseService(oneRosterService)

    def "getAllCourses returns a list of courses"() {
        when:
        def courses = courseService.getAllCourses()

        then:
        courses.size() == 2
        courses.first().sourcedId == 'level-sourced-id'
        courses.first().title == 'Level'
        courses.first().org.sourcedId == 'school-owner-sourced-id'

        courses.last().sourcedId == 'subject-sourced-id'
        courses.last().title == 'Subject'
        courses.last().org.sourcedId == 'school-owner-sourced-id'
    }

    def "getCourse returns a course given valid sourcedId"() {
        when:
        def course = courseService.getCourse('level-sourced-id')

        then:
        course.sourcedId == 'level-sourced-id'
        course.title == 'Level'
        course.org.sourcedId == 'school-owner-sourced-id'
    }

    List<Course> getCourses() {
        Course level = new Course(
                'level-sourced-id',
                'Level',
                GUIDRef.of(GUIDType.ORG, 'school-owner-sourced-id')
        )

        Course subject = new Course(
                'subject-sourced-id',
                'Subject',
                GUIDRef.of(GUIDType.ORG, 'school-owner-sourced-id')
        )

        return [level, subject]
    }
}

