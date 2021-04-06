package no.fint.oneroster.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;

@Data
@ConfigurationProperties("oneroster")
public class OneRosterProperties {
    private Org org;
    private AcademicSession academicSession;
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

    @Data
    public static class AcademicSession {
        private Term firstTerm;
        private Term secondTerm;
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