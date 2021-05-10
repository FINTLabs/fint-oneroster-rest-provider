package no.fint.oneroster.factory

import no.fint.oneroster.properties.OneRosterProperties
import no.fint.oneroster.FintObjectFactory
import spock.lang.Specification

class CourseFactorySpec extends Specification {

    OneRosterProperties.Org org = new OneRosterProperties.Org(sourcedId: 'school-owner-sourced-id', name: 'School owner')

    def "level() returns course object"() {
        when:
        def level = CourseFactory.level(FintObjectFactory.newLevel(), org)

        then:
        level.sourcedId == 'level-sourced-id'
        level.title == 'Level'
        level.org.sourcedId == 'school-owner-sourced-id'
    }

    def "subject() returns course object"() {
        when:
        def level = CourseFactory.subject(FintObjectFactory.newSubject(), org)

        then:
        level.sourcedId == 'subject-sourced-id'
        level.title == 'Subject'
        level.org.sourcedId == 'school-owner-sourced-id'
    }
}
