package no.fint.oneroster.configuration;

import no.fint.oneroster.properties.OneRosterProperties;
import no.fint.oneroster.security.ClientValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

@Configuration
@ConditionalOnProperty(prefix = "oneroster", name = "access-control", havingValue = "true")
public class AccessControlSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final OneRosterProperties oneRosterProperties;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSet;

    public AccessControlSecurityConfiguration(OneRosterProperties oneRosterProperties) {
        this.oneRosterProperties = oneRosterProperties;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer(configurer -> configurer.jwt().decoder(jwtDecoder()));
    }

    private JwtDecoder jwtDecoder() {
        OAuth2TokenValidator<Jwt> client = new ClientValidator(oneRosterProperties.getClientIds());
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(client);

        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(jwkSet).build();
        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }
}

