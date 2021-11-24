package no.fint.oneroster.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.ZonedDateTime;
import java.util.Map;

@ToString
@Getter
@Setter
@EqualsAndHashCode(exclude = "dateLastModified")
@JsonFilter("fields")
public class Base {
    private final String sourcedId;
    private final StatusType status;
    private final ZonedDateTime dateLastModified;
    private Map<String, String> metadata;

    public Base(String sourcedId, StatusType status, ZonedDateTime dateLastModified) {
        this.sourcedId = sourcedId;
        this.status = status;
        this.dateLastModified = dateLastModified;
    }
}