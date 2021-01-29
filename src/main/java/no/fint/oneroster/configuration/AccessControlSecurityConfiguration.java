package no.fint.oneroster.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import no.fint.oneroster.properties.FintProperties;
import no.fint.oneroster.properties.OneRosterProperties;
import no.fint.oneroster.jwt.JWTFilter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.text.ParseException;

@Configuration
@ConditionalOnProperty(name = "oneroster.access-control", havingValue = "true")
public class AccessControlSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final FintProperties fintProperties;
    private final OneRosterProperties oneRosterProperties;

    public AccessControlSecurityConfiguration(FintProperties fintProperties, OneRosterProperties oneRosterProperties) {
        this.fintProperties = fintProperties;
        this.oneRosterProperties = oneRosterProperties;

        Security.addProvider(new BouncyCastleProvider());
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
                .addFilterBefore(new JWTFilter(signingKey(), encryptionKey(), oneRosterProperties.getClientIds()), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public RSAKey signingKey() throws IOException, ParseException {
        JWKSet jwkSet = JWKSet.load(new URL(fintProperties.getSigningKeys()));

        return jwkSet.getKeys().get(0).toRSAKey();
    }

    @Bean
    public PrivateKey encryptionKey() throws IOException {
        PEMParser pemParser = new PEMParser(new InputStreamReader(new FileInputStream(fintProperties.getEncryptionKeys())));
        PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();

        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        KeyPair keyPair = converter.getKeyPair(pemKeyPair);

        pemParser.close();

        return keyPair.getPrivate();
    }
}
