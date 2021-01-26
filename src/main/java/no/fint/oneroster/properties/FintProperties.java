package no.fint.oneroster.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties("fint")
public class FintProperties {
    private Map<String, Component> component;
    private String signingKeys;
    private String encryptionKeys;

    @Data
    public static class Component {
        private Map<String, Registration> registration;
        private Map<String, String> endpoint;
    }

    @Data
    public static class Registration {
        private String id;
        private String username;
        private String password;
    }
}
