package no.fint.oneroster.repository;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.AbstractCollectionResources;
import no.fint.oneroster.properties.OrganisationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Slf4j
@Repository
public class FintRepository {
    private final WebClient webClient;
    private final Authentication principal;
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final OrganisationProperties organisationProperties;

    public FintRepository(WebClient webClient, Authentication principal, OAuth2AuthorizedClientManager authorizedClientManager, OrganisationProperties organisationProperties) {
        this.webClient = webClient;
        this.principal = principal;
        this.authorizedClientManager = authorizedClientManager;
        this.organisationProperties = organisationProperties;
    }

    public <S, T extends AbstractCollectionResources<S>> Flux<S> getResources(Class<T> clazz, String componentKey, String endpointKey) {
        OrganisationProperties.Component component = organisationProperties.getOrganisation().getComponents().get(componentKey);

        if (component.getRegistrations().size() == 2) {
            return Flux.merge(
                    get(clazz, component.getRegistrations().get(0), component.getEndpoints().get(endpointKey))
                            .flatMapIterable(T::getContent),
                    get(clazz, component.getRegistrations().get(1), component.getEndpoints().get(endpointKey))
                            .flatMapIterable(T::getContent)
            );
        }

        return get(clazz, component.getRegistrations().get(0), component.getEndpoints().get(endpointKey))
                .flatMapIterable(T::getContent);
    }

    public <T> Mono<T> get(Class<T> clazz, OrganisationProperties.Registration registration, String endpoint) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(registration.getId())
                .principal(principal)
                .attributes(attrs -> {
                    attrs.put(OAuth2ParameterNames.USERNAME, registration.getUsername());
                    attrs.put(OAuth2ParameterNames.PASSWORD, registration.getPassword());
                }).build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        return webClient.get()
                .uri(endpoint)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(clazz)
                .doOnSuccess(it -> log.info("Updated {}...", endpoint));
    }
}
