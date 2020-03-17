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
    MockMvc mockMvc = MockMvcBuilders.standaloneSetup(orgController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build()

    def "Given valid orgId return list of orgs"() {
        when:
        def response = mockMvc.perform(get("/orgs").header('orgId', '12345'))

        then:
        1 * orgService.getAllOrgs('12345') >> getOrgs()
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.orgs[0].sourcedId').value('12'))
    }

    def "Given fields parameter returns list of orgs with only selected fields"() {
        when:
        def response = mockMvc.perform(get("/orgs").header('orgId', '12345').param('fields', 'sourcedId'))

        then:
        1 * orgService.getAllOrgs('12345') >> getOrgs()
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.orgs[0].sourcedId').value('12'))
                .andExpect(jsonPath('$.orgs[0].name').doesNotHaveJsonPath())
    }


    List<Org> getOrgs() {
        return [new Org('12', 'abc', OrgType.SCHOOL),
                new Org('34', 'def', OrgType.SCHOOL),
                new Org('56', 'ghi', OrgType.SCHOOL),
                new Org('78', 'jkl', OrgType.SCHOOL)]
    }
}
