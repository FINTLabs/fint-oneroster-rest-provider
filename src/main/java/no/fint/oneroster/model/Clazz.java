package no.fint.oneroster.model;

import lombok.Getter;
import lombok.Setter;
import no.fint.oneroster.model.vocab.ClazzType;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Clazz extends Base {
    private final String title;
    private final ClazzType clazzType;
    private final GUIDRef course;
    private final GUIDRef school;
    private final List<GUIDRef> terms;
    private String classCode;
    private String location;
    private List<String> grades;
    private List<String> subjects;
    private List<String> subjectCodes;
    private List<String> periods;

    public Clazz(String sourcedId, String title, ClazzType clazzType, GUIDRef course, GUIDRef school, List<GUIDRef> terms) {
        super(sourcedId, StatusType.ACTIVE, ZonedDateTime.now(ZoneId.of("Z")));
        this.title = title;
        this.clazzType = clazzType;
        this.course = course;
        this.school = school;
        this.terms = new ArrayList<>(terms);
    }
}
