package no.fint.oneroster.service

import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.properties.OneRosterProperties
import no.fint.oneroster.repository.FintEducationService
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

class OrgServiceSpec extends Specification {

    FintEducationService fintEducationService = Mock {
        getSchools() >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
    }

    OneRosterProperties organisationProperties = Mock {
        getOrg() >> new OneRosterProperties.Org(
                sourcedId: 'school-owner-sourced-id',
                name: 'School owner',
                identifier: '0123456789'
        )
    }

    OrgService orgService = new OrgService(fintEducationService, organisationProperties)

    def "getAllOrgs returns a list of orgs given valid orgId"() {
        when:
        def orgs = orgService.getAllOrgs()

        then:
        orgs.size() == 2
    }

    def "getOrg returns an org given valid orgId and sourcedId"() {
        when:
        def org = orgService.getOrg('school-owner-sourced-id')

        then:
        org.sourcedId == 'school-owner-sourced-id'
        org.name == 'School owner'
        org.type == OrgType.DISTRICT
        org.identifier == '0123456789'
        org.children.first().sourcedId == 'school-sourced-id'
    }

    def "getAllSchools returns a list of schools given valid orgId"() {
        when:
        def schools = orgService.getAllSchools()

        then:
        schools.size() == 1
    }

    def "getSchool returns a school given valid orgId and sourcedId"() {
        when:
        def school = orgService.getSchool('school-sourced-id')

        then:
        school.sourcedId == 'school-sourced-id'
        school.name == 'School'
        school.type == OrgType.SCHOOL
        school.identifier == 'identifier'
        school.parent.sourcedId == 'school-owner-sourced-id'
    }
}
