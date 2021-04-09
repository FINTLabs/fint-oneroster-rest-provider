package no.fint.oneroster.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties("oneroster")
public class OneRosterProperties {
    private Org org;
    private boolean contactTeacherGroups;
    private String classFactory;
    private String userFactory;
    private boolean accessControl;
    private Map<String, Client> clients;

    @Data
    public static class Org {
        private String sourcedId;
        private String name;
        private String identifier;
    }

    @Data
    public static class Client {
        private String id;
        private String name;
    }
}