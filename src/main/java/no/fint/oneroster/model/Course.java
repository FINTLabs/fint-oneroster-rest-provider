package no.fint.oneroster.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Course extends Base {
    private final String title;
    private final GUIDRef org;
    private GUIDRef schoolYear;
    private String courseCode;
    private List<String> grades;
    private List<String> subjects;
    private List<String> subjectCodes;

    public Course(String sourcedId, String title, GUIDRef org) {
        super(sourcedId, StatusType.ACTIVE, ZonedDateTime.now(ZoneId.of("Z")));
        this.title = title;
        this.org = org;
    }
}
