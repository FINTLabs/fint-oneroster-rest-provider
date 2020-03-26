package no.fint.oneroster.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties("fint")
public class OrganisationProperties {

    private Map<String, Organisation> organisations = new HashMap<>();

    @Data
    public static class Organisation {
        private String sourcedId;
        private String name;
        private String identifier;
        private SchoolYear schoolYear;
        private String username;
        private String password;
        private String registration;
        private String environment;
    }

    @Data
    public static class SchoolYear {
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