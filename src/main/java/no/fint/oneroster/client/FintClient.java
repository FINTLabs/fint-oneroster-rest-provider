package no.fint.oneroster.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.AbstractCollectionResources;
import no.fint.oneroster.properties.FintProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Slf4j
@Component
public class FintClient {
    private final WebClient webClient;
    private final Authentication principal;
    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final FintProperties fintProperties;

    private final Map<String, Long> sinceTimestamp = new ConcurrentHashMap<>();

    public FintClient(WebClient webClient, Authentication principal, OAuth2AuthorizedClientManager authorizedClientManager, FintProperties fintProperties) {
        this.webClient = webClient;
        this.principal = principal;
        this.authorizedClientManager = authorizedClientManager;
        this.fintProperties = fintProperties;
    }

    public <S, T extends AbstractCollectionResources<S>> Flux<S> getEducationResources(Class<T> clazz, String endpoint) {
        return getResources(clazz, FintComponent.EDUCATION.getKey(), endpoint);
    }

    public <S, T extends AbstractCollectionResources<S>> Flux<S> getAdministrationResources(Class<T> clazz, String endpoint) {
        return getResources(clazz, FintComponent.ADMINISTRATION.getKey(), endpoint);
    }

    private <S, T extends AbstractCollectionResources<S>> Flux<S> getResources(Class<T> clazz, String componentKey, String endpointKey) {
        FintProperties.Component component = fintProperties.getComponent().get(componentKey);

        return Flux.concat(component.getRegistration().values()
                .stream()
                .map(credential -> get(clazz, credential, component.getEndpoint().get(endpointKey))
                        .flatMapIterable(T::getContent))
                .collect(Collectors.toList()));
    }

    private <T> Mono<T> get(Class<T> clazz, FintProperties.Registration credential, String endpoint) {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest.withClientRegistrationId(credential.getId())
                .principal(principal)
                .attributes(attrs -> {
                    attrs.put(OAuth2ParameterNames.USERNAME, credential.getUsername());
                    attrs.put(OAuth2ParameterNames.PASSWORD, credential.getPassword());
                }).build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        log.info("OAuth2: registration-id = {}, client-id = {}",
                authorizedClient.getClientRegistration().getRegistrationId(),
                authorizedClient.getClientRegistration().getClientId());

        return webClient.get()
                .uri(endpoint.concat("/last-updated"))
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(LastUpdated.class)
                .flatMap(lastUpdated -> webClient.get()
                        .uri(endpoint, uriBuilder -> uriBuilder.queryParam("sinceTimeStamp", sinceTimestamp.getOrDefault(endpoint, 0L)).build())
                        .attributes(oauth2AuthorizedClient(authorizedClient))
                        .retrieve()
                        .bodyToMono(clazz)
                        .doOnNext(it -> sinceTimestamp.put(endpoint, lastUpdated.getLastUpdated())));
    }

    @Data
    private static class LastUpdated {
        private Long lastUpdated;
    }

    public void reset() {
        sinceTimestamp.clear();
    }
}