package no.fint.oneroster.repository

import com.fasterxml.jackson.databind.ObjectMapper
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResources
import no.fint.oneroster.properties.OrganisationProperties
import no.fint.oneroster.util.FintObjectFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.web.reactive.function.client.WebClient
import spock.lang.Specification

class FintAdministrationServiceSpec extends Specification {
    MockWebServer mockWebServer = new MockWebServer()
    WebClient webClient = WebClient.builder().build()

    OAuth2AuthorizedClientManager authorizedClientManager = Mock {
        //1 * authorize(_) >> Mock(OAuth2AuthorizedClient)
    }

    OrganisationProperties organisationProperties = Mock {
        1 * getOrganisation() >> new OrganisationProperties.Organisation(
                username: _ as String, password: _ as String, registration: _ as String, endpoints: [('school'): mockWebServer.url("/").toString()]
        )
    }

    FintAdministrationService fintRepository = new FintAdministrationService(webClient, Mock(Authentication), authorizedClientManager, organisationProperties)

    def "get() for given type returns resources of given type"() {
        given:
        def schools = new SkoleResources()
        schools.addResource(FintObjectFactory.newSchool())
        mockWebServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(schools))
                .setHeader('content-type', 'application/json')
                .setResponseCode(200))

        when:
        def resources = fintRepository.getSchools()

        then:
        resources.size() == 1
    }
}

