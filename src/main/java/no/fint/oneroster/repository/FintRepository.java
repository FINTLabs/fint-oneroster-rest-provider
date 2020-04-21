package no.fint.oneroster.repository;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.AbstractCollectionResources;
import no.fint.oneroster.properties.FintProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Slf4j
@Repository
public class FintRepository {
    private final WebClient webClient;
    private final Authentication principal;
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final FintProperties fintProperties;

    public FintRepository(WebClient webClient, Authentication principal, OAuth2AuthorizedClientManager authorizedClientManager, FintProperties fintProperties) {
        this.webClient = webClient;
        this.principal = principal;
        this.authorizedClientManager = authorizedClientManager;
        this.fintProperties = fintProperties;
    }

    public <S, T extends AbstractCollectionResources<S>> Flux<S> getResources(Class<T> clazz, String componentKey, String endpointKey) {
        FintProperties.Component component = fintProperties.getComponent().get(componentKey);

        return Flux.merge(component.getRegistration().values()
                .stream()
                .map(credential ->
                        get(clazz, credential, component.getEndpoint().get(endpointKey))
                                .flatMapIterable(T::getContent))
                .collect(Collectors.toList()));
    }

    public <T> Mono<T> get(Class<T> clazz, FintProperties.Registration credential, String endpoint) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(credential.getId())
                .principal(principal)
                .attributes(attrs -> {
                    attrs.put(OAuth2ParameterNames.USERNAME, credential.getUsername());
                    attrs.put(OAuth2ParameterNames.PASSWORD, credential.getPassword());
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
