package no.fint.oneroster.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .permitAll();
    }

    @Bean
    public Authentication dummyAuthentication() {
        return new UsernamePasswordAuthenticationToken("ims", "oneroster", Collections.emptyList());
    }

    @Bean
    public OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                 OAuth2AuthorizedClientService authorizedClientService) {

        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()
                .password().refreshToken().build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        authorizedClientManager.setContextAttributesMapper(contextAttributesMapper());

        return authorizedClientManager;
    }

    private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributesMapper() {
        return authorizeRequest -> {
            Map<String, Object> contextAttributes = new HashMap<>();
            contextAttributes.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, authorizeRequest.getAttribute(OAuth2ParameterNames.USERNAME));
            contextAttributes.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, authorizeRequest.getAttribute(OAuth2ParameterNames.PASSWORD));
            return contextAttributes;
        };
    }

    @Bean
    public ClientHttpConnector clientHttpConnector() {
        return new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection()));
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder, OAuth2AuthorizedClientManager authorizedClientManager, ClientHttpConnector clientHttpConnector) {
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

        ServletOAuth2AuthorizedClientExchangeFilterFunction authorizedClientExchangeFilterFunction =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        return builder
                .clientConnector(clientHttpConnector)
                .exchangeStrategies(exchangeStrategies)
                .filter(authorizedClientExchangeFilterFunction)
                .build();
    }
}
