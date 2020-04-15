package no.fint.oneroster.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import no.fint.oneroster.model.vocab.SessionType;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public class AcademicSession extends Base {
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final SessionType type;
    private final Year schoolYear;
    private GUIDRef parent;
    private List<GUIDRef> children;

    public AcademicSession(String sourcedId, String title, LocalDate startDate, LocalDate endDate, SessionType type, Year schoolYear) {
        super(sourcedId, StatusType.ACTIVE, ZonedDateTime.now(ZoneId.of("Z")));
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.schoolYear = schoolYear;
    }
}
