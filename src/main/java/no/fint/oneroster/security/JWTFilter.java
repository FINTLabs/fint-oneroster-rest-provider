package no.fint.oneroster.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.properties.OneRosterProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
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
@Component
public class JWTFilter extends OncePerRequestFilter {
    private final OneRosterProperties oneRosterProperties;
    private final JWTUtil jwtUtil;

    public JWTFilter(OneRosterProperties oneRosterProperties, JWTUtil jwtUtil) {
        this.oneRosterProperties = oneRosterProperties;
        this.jwtUtil = jwtUtil;
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

            JWTClaimsSet jwtClaimsSet = jwtUtil.getClaimsSet(token);

            String clientId = jwtClaimsSet.getSubject();

            if (isEmpty(clientId)) {
                return;
            }

            if (Arrays.asList(oneRosterProperties.getClientIds()).contains(clientId)) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("ims", "oneroster", Collections.emptyList()));
            }

        } catch (ParseException | JOSEException | IllegalStateException ex) {
            log.warn(ex.getMessage());
        }

        chain.doFilter(request, response);
    }
}
