package no.fint.oneroster.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import no.fint.oneroster.model.vocab.OrgType;
import no.fint.oneroster.model.vocab.StatusType;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Getter @Setter
@EqualsAndHashCode(callSuper = true)
public class Org extends Base {
    private final String name;
    private final OrgType type;
    private String identifier;
    private GUIDRef parent;
    private List<GUIDRef> children;

    public Org(String sourcedId, String name, OrgType type) {
        super(sourcedId, StatusType.ACTIVE, ZonedDateTime.now(ZoneId.of("Z")));
        this.name = name;
        this.type = type;
    }
}