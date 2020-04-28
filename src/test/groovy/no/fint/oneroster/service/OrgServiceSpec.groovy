package no.fint.oneroster.service

import no.fint.oneroster.model.Org
import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.repository.OneRosterService
import spock.lang.Specification

class OrgServiceSpec extends Specification {

    OneRosterService oneRosterService = Mock {
        getAllOrgs() >> getOrgs()
    }

    OrgService orgService = new OrgService(oneRosterService)

    def "getAllOrgs returns a list of orgs"() {
        when:
        def orgs = orgService.getAllOrgs()

        then:
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
        org.sourcedId == 'school-owner-sourced-id'
        org.name == 'School owner'
        org.type == OrgType.DISTRICT
    }

    def "getAllSchools returns a list of schools"() {
        when:
        def schools = orgService.getAllSchools()

        then:
        schools.size() == 1
    }

    def "getSchool returns a school given valid sourcedId"() {
        when:
        def school = orgService.getSchool('school-sourced-id')

        then:
        school.sourcedId == 'school-sourced-id'
        school.name == 'School'
        school.type == OrgType.SCHOOL
    }

    List<Org> getOrgs() {
        Org schoolOwner = new Org(
                'school-owner-sourced-id',
                'School owner',
                OrgType.DISTRICT
        )

        Org school = new Org(
                'school-sourced-id',
                'School',
                OrgType.SCHOOL
        )

        return [schoolOwner, school]
    }
}
