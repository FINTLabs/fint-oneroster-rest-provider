package no.fint.oneroster.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import no.fint.oneroster.model.vocab.RoleType;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter @Setter
public class Enrollment extends Base {
    private final GUIDRef user;
    private final GUIDRef clazz;
    private final GUIDRef school;
    private final RoleType role;
    private Boolean primary;
    private LocalDate beginDate;
    private LocalDate endDate;

    public Enrollment(String sourcedId, GUIDRef user, GUIDRef clazz, GUIDRef school, RoleType role) {
        super(sourcedId, StatusType.ACTIVE, ZonedDateTime.now(ZoneId.of("Z")));
        this.user = user;
        this.clazz = clazz;
        this.school = school;
        this.role = role;
    }

    @JsonProperty("class")
    public GUIDRef getClazz() {
        return clazz;
    }
}
