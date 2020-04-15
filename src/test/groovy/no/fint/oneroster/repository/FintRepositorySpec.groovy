package no.fint.oneroster.repository

import com.fasterxml.jackson.databind.ObjectMapper
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResources
import no.fint.oneroster.properties.OrganisationProperties
import no.fint.oneroster.util.FintObjectFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class FintRepositorySpec extends Specification {
    MockWebServer mockWebServer = new MockWebServer()
    WebClient webClient = WebClient.builder().build()

    OAuth2AuthorizedClientManager authorizedClientManager = Mock {
        1 * authorize(_ as OAuth2AuthorizeRequest) >> Mock(OAuth2AuthorizedClient)
    }

    OrganisationProperties organisationProperties = Mock {
        1 * getOrganisation() >> new OrganisationProperties.Organisation(components: [('education'): new OrganisationProperties.Component(
                registrations: [new OrganisationProperties.Registration(id: _ as String, username: _ as String, password: _ as String)],
                endpoints: [('school'): mockWebServer.url("/").toString()]
        )])
    }

    FintRepository fintRepository = new FintRepository(webClient, Mock(Authentication), authorizedClientManager, organisationProperties)

    def "get() for given type returns resources of given type"() {
        given:
        def schools = new SkoleResources()
        schools.addResource(FintObjectFactory.newSchool())
        mockWebServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(schools))
                .setHeader('content-type', 'application/json')
                .setResponseCode(200))

        when:
        def resources = fintRepository.getResources(SkoleResources.class, 'education', 'school')

        then:
        resources.blockLast().navn == 'School'
    }
}

