package no.fint.oneroster.repository

import com.fasterxml.jackson.databind.ObjectMapper
import no.fint.model.resource.utdanning.utdanningsprogram.SkoleResources
import no.fint.oneroster.properties.FintProperties
import no.fint.oneroster.util.FintObjectFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier
import spock.lang.Specification

class FintRepositorySpec extends Specification {
    MockWebServer mockWebServer = new MockWebServer()
    WebClient webClient = WebClient.builder().build()

    OAuth2AuthorizedClientManager authorizedClientManager = Stub() {
        authorize(_ as OAuth2AuthorizeRequest) >> Mock(OAuth2AuthorizedClient)
    }

    FintProperties fintProperties = Stub() {
        getComponent() >> [('education'): new FintProperties.Component(
                registration: [('1'): new FintProperties.Registration(id: _ as String, username: _ as String, password: _ as String)],
                endpoint: [('school'): mockWebServer.url("/").toString()]
        )]
    }

    FintRepository fintRepository = new FintRepository(webClient, Mock(Authentication), authorizedClientManager, fintProperties)

    def "get() for given type returns resources of given type"() {
        given:
        def schools = new SkoleResources()
        schools.addResource(FintObjectFactory.newSchool())
        mockWebServer.enqueue(new MockResponse()
                .setBody(new ObjectMapper().writeValueAsString(schools))
                .setHeader('content-type', 'application/json')
                .setResponseCode(200))

        when:
        def resources = fintRepository.getEducationResources(SkoleResources.class, 'school')

        then:
        StepVerifier.create(resources).expectNextCount(1).verifyComplete()
    }
}

