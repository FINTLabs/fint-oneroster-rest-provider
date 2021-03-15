package no.fint.oneroster.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final RSAKey signingKey;

    private final String[] clientIds;

    public JWTFilter(RSAKey signingKey, String[] clientIds) {
        this.signingKey = signingKey;
        this.clientIds = clientIds;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (isEmpty(authorization) || !authorization.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = authorization.split(" ")[1].trim();

            JWTClaimsSet jwtClaimsSet = getJWTClaimsSet(token);

            String clientId = jwtClaimsSet.getSubject();

            if (isEmpty(clientId)) {
                return;
            }

            if (Arrays.asList(clientIds).contains(clientId)) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("oneroster", "client", Collections.emptyList()));
            }

        } catch (ParseException | JOSEException | IllegalStateException ex) {
            log.warn(ex.getMessage());
        }

        chain.doFilter(request, response);
    }

    private JWTClaimsSet getJWTClaimsSet(String token) throws ParseException, JOSEException, IllegalStateException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verified = signedJWT.verify(new RSASSAVerifier(signingKey));

        if (!verified) {
            throw new JOSEException("Verification of signed JWT failed");
        }

        return signedJWT.getJWTClaimsSet();
    }
}
