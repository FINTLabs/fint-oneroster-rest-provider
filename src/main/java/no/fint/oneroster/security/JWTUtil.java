package no.fint.oneroster.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import no.fint.oneroster.properties.FintProperties;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyPair;
import java.security.Security;
import java.text.ParseException;

@Component
public class JWTUtil {
    private final FintProperties fintProperties;

    private JWKSet signingKeys;
    private KeyPair encryptionKeys;

    public JWTUtil(FintProperties fintProperties) {
        this.fintProperties = fintProperties;
    }

    public JWTClaimsSet getClaimsSet(String token) throws ParseException, JOSEException, IllegalStateException {
        EncryptedJWT encryptedJWT = EncryptedJWT.parse(token);
        encryptedJWT.decrypt(new RSADecrypter(encryptionKeys.getPrivate()));

        SignedJWT signedJWT = encryptedJWT.getPayload().toSignedJWT();
        boolean verified = signedJWT.verify(new RSASSAVerifier(signingKeys.getKeys().get(0).toRSAKey()));

        if (!verified) {
            throw new JOSEException("Verification of signed JWT failed");
        }

        return signedJWT.getJWTClaimsSet();
    }

    @PostConstruct
    public void init() throws IOException, ParseException {
        Security.addProvider(new BouncyCastleProvider());

        signingKeys = JWKSet.load(new URL(fintProperties.getSigningKeys()));

        PEMParser pemParser = new PEMParser(new InputStreamReader(new FileInputStream(fintProperties.getEncryptionKeys())));
        PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();

        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        encryptionKeys = converter.getKeyPair(pemKeyPair);

        pemParser.close();
    }
}
