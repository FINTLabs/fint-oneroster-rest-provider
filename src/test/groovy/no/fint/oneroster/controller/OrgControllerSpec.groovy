package no.fint.oneroster.controller

import no.fint.oneroster.model.Org
import no.fint.oneroster.model.vocab.OrgType
import no.fint.oneroster.service.OrgService
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

class OrgControllerSpec extends Specification {

    OrgService orgService = Mock()
    OrgController orgController = new OrgController(orgService)
    MockMvc mockMvc
    PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver()

    void setup() {
        pageableHandlerMethodArgumentResolver.setPageParameterName("offset")
        pageableHandlerMethodArgumentResolver.setSizeParameterName("limit")
        mockMvc = MockMvcBuilders.standaloneSetup(orgController)
                .setCustomArgumentResolvers(pageableHandlerMethodArgumentResolver)
                .build()
    }

    def "Given valid orgId return list of orgs"() {
        when:
        def response = mockMvc.perform(get("/orgs").header('orgId', _ as String))

        then:
        1 * orgService.getAllOrgs(_ as String) >> getOrgs()
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.orgs[0].sourcedId').value('12'))
    }

    def "Given fields parameter returns list of orgs with only selected fields"() {
        when:
        def response = mockMvc.perform(get("/orgs").header('orgId', _ as String).param('fields', 'sourcedId'))

        then:
        1 * orgService.getAllOrgs(_ as String) >> getOrgs()
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.orgs[0].sourcedId').value('12'))
                .andExpect(jsonPath('$.orgs[0].name').doesNotHaveJsonPath())
    }

    def "Given offset parameter returns sublist of orgs"() {
        when:
        def response = mockMvc.perform(get("/orgs").header('orgId', _ as String).param('offset', '0'))

        then:
        1 * orgService.getAllOrgs(_ as String) >> getOrgs()
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.orgs.length()').value('4'))
    }

    def "Given limit parameter returns sublist of orgs"() {
        when:
        def response = mockMvc.perform(get("/orgs").header('orgId', _ as String).param('limit', '1'))

        then:
        1 * orgService.getAllOrgs(_ as String) >> getOrgs()
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.orgs.length()').value('1'))
    }

    def "Given sort parameter return sorted list of orgs"() {
        when:
        def response = mockMvc.perform(get("/orgs").header('orgId', _ as String).param('sort', 'name'))

        then:
        1 * orgService.getAllOrgs(_ as String) >> getOrgs()
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.orgs[0].name').value('abc'))
    }

    def "Given filter parameter return sublist of orgs meeting criteria"() {
        when:
        def response = mockMvc.perform(get("/orgs").header('orgId', _ as String).param('filter', 'name~\'b\''))

        then:
        1 * orgService.getAllOrgs(_ as String) >> getOrgs()
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.orgs.length()').value('1'))
                .andExpect(jsonPath('$.orgs[0].name').value('abc'))
    }

    def "Given valid sourcedId return org"() {
        when:
        def response = mockMvc.perform(get("/orgs/12").header('orgId', _ as String))

        then:
        1 * orgService.getOrg(_ as String, '12') >> getOrgs().first()
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.org.name').value('jkl'))
    }

    def "Given valid sourcedId and fields parameter return org with only selected fields"() {
        when:
        def response = mockMvc.perform(get("/orgs/12").header('orgId', _ as String).param('fields', 'sourcedId'))

        then:
        1 * orgService.getOrg(_ as String, '12') >> getOrgs().first()
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.org.sourcedId').value('12'))
                .andExpect(jsonPath('$.org.name').doesNotHaveJsonPath())
    }

    List<Org> getOrgs() {
        return [new Org('12', 'jkl', OrgType.SCHOOL),
                new Org('34', 'ghi', OrgType.SCHOOL),
                new Org('56', 'def', OrgType.SCHOOL),
                new Org('78', 'abc', OrgType.SCHOOL)]
    }
}
