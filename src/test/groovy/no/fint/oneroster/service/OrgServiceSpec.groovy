package no.fint.oneroster.service

import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.repository.FintRepository
import no.fint.oneroster.util.FintObjectFactory
import spock.lang.Specification

class OrgServiceSpec extends Specification {

    FintRepository fintRepository = Mock()
    OrgService orgService = new OrgService(fintRepository)

    def "getAllOrgs returns a list of orgs given valid orgId"() {
        when:
        def orgs = orgService.getAllOrgs(_ as String)

        then:
        1 * fintRepository.getOrganisationalElements(_ as String) >> [('/school-owner-sourced-id'): FintObjectFactory.newSchoolOwner()]
        1 * fintRepository.getSchools(_ as String) >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
        orgs.size() == 2
    }

    def "getOrg returns an org given valid orgId and sourcedId"() {
        when:
        def org = orgService.getOrg(_ as String, 'school-owner-sourced-id')

        then:
        1 * fintRepository.getOrganisationalElements(_ as String) >> [('/school-owner-sourced-id'): FintObjectFactory.newSchoolOwner()]
        1 * fintRepository.getSchools(_ as String) >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
        org.sourcedId == 'school-owner-sourced-id'
        org.name == 'SchoolOwner'
        org.type == OrgType.DISTRICT
        org.identifier == 'identifier'
        org.children.first().sourcedId == 'school-sourced-id'
    }

    def "getAllSchools returns a list of schools given valid orgId"() {
        when:
        def schools = orgService.getAllSchools(_ as String)

        then:
        1 * fintRepository.getOrganisationalElements(_ as String) >> [('/school-owner-sourced-id'): FintObjectFactory.newSchoolOwner()]
        1 * fintRepository.getSchools(_ as String) >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
        schools.size() == 1
    }

    def "getSchool returns a school given valid orgId and sourcedId"() {
        when:
        def school = orgService.getSchool(_ as String, 'school-sourced-id')

        then:
        1 * fintRepository.getOrganisationalElements(_ as String) >> [('/school-owner-sourced-id'): FintObjectFactory.newSchoolOwner()]
        1 * fintRepository.getSchools(_ as String) >> [('/school-sourced-id'): FintObjectFactory.newSchool()]
        school.sourcedId == 'school-sourced-id'
        school.name == 'School'
        school.type == OrgType.SCHOOL
        school.identifier == 'identifier'
        school.parent.sourcedId == 'school-owner-sourced-id'
    }

    def "isSchoolOwner returns true when self link is contained in parent link"() {
        when:
        def schoolOwner = orgService.isSchoolOwner().test(FintObjectFactory.newSchoolOwner())

        then:
        schoolOwner
    }

    def "getSchoolOwner returns a school owner when an organisation element is school owner and contains required fields"() {
        when:
        def schoolOwner = orgService.getSchoolOwner(_ as String)

        then:
        1 * fintRepository.getOrganisationalElements(_ as String) >> [(_ as String): FintObjectFactory.newSchoolOwner()]
        schoolOwner.isPresent()
    }
}
