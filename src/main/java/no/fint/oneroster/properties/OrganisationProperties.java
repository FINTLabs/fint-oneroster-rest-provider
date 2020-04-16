package no.fint.oneroster.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties("fint")
public class OrganisationProperties {
    private Organisation organisation = new Organisation();

    @Data
    public static class Organisation {
        private String sourcedId;
        private String name;
        private String identifier;
        private SchoolYear schoolYear;
        private Map<String, Component> components = new HashMap<>();
    }

    @Data
    public static class Component {
        private List<Registration> registrations = new ArrayList<>();
        private Map<String, String> endpoints = new HashMap<>();
    }

    @Data
    public static class Registration {
        private String id;
        private String username;
        private String password;
    }

    @Data
    public static class SchoolYear {
        private String sourcedId;
        private LocalDate beginDate;
        private LocalDate endDate;
        private String name;
        private Term firstTerm;
        private Term secondTerm;

        public void setBeginDate(String beginDate) {
            this.beginDate = LocalDate.parse(beginDate);
        }

        public void setEndDate(String endDate) {
            this.endDate = LocalDate.parse(endDate);
        }
    }

    @Data
    public static class Term {
        private String sourcedId;
        private LocalDate beginDate;
        private LocalDate endDate;
        private String name;

        public void setBeginDate(String beginDate) {
            this.beginDate = LocalDate.parse(beginDate);
        }

        public void setEndDate(String endDate) {
            this.endDate = LocalDate.parse(endDate);
        }
    }
}