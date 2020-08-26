package no.fint.oneroster.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import no.fint.oneroster.model.vocab.ClazzType;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public class Clazz extends Base {
    private final String title;
    private final ClazzType classType;
    private final GUIDRef course;
    private final GUIDRef school;
    private final List<GUIDRef> terms;
    private String classCode;
    private String location;
    private List<String> grades;
    private List<String> subjects;
    private List<String> subjectCodes;
    private List<String> periods;

    public Clazz(String sourcedId, StatusType statusType, String title, ClazzType classType, GUIDRef course, GUIDRef school, List<GUIDRef> terms) {
        super(sourcedId, statusType, ZonedDateTime.now(ZoneId.of("Z")));
        this.title = title;
        this.classType = classType;
        this.course = course;
        this.school = school;
        this.terms = new ArrayList<>(terms);
    }
}
