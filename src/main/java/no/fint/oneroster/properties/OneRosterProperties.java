package no.fint.oneroster.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("oneroster")
public class OneRosterProperties {
    private Org org;
    private boolean contactTeacherGroups;
    private String classFactory;
    private String userFactory;
    private boolean accessControl;
    private String[] clientIds;

    @Data
    public static class Org {
        private String sourcedId;
        private String name;
        private String identifier;
    }
}