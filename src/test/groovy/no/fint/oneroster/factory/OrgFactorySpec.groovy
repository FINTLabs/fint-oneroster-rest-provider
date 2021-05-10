package no.fint.oneroster.factory

import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.properties.OneRosterProperties
import no.fint.oneroster.FintObjectFactory
import spock.lang.Specification

class OrgFactorySpec extends Specification {

    def "school() returns org object of type school"() {
        when:
        def school = OrgFactory.school(FintObjectFactory.newSchool())

        then:
        school.sourcedId == 'school-sourced-id'
        school.name == 'School'
        school.type == OrgType.SCHOOL
    }

    def "schoolOwner() returns org object of type district"() {
        given:
        OneRosterProperties.Org org = new OneRosterProperties.Org(sourcedId: 'school-owner-sourced-id', name: 'School owner')

        when:
        def schoolOwner = OrgFactory.schoolOwner(org)

        then:
        schoolOwner.sourcedId == 'school-owner-sourced-id'
        schoolOwner.name == 'School owner'
        schoolOwner.type == OrgType.DISTRICT
    }
}
