package no.fint.oneroster.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public class ClientValidator implements OAuth2TokenValidator<Jwt> {
    private final String[] clientIds;

    public ClientValidator(String[] clientIds) {
        this.clientIds = clientIds;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        if (List.of(clientIds).contains(jwt.getSubject())) {
            return OAuth2TokenValidatorResult.success();
        }

        OAuth2Error err = new OAuth2Error(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        return OAuth2TokenValidatorResult.failure(err);
    }
}
