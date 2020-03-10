package no.fint.oneroster.model;

import lombok.Getter;
import lombok.Setter;
import no.fint.oneroster.model.vocab.GUIDType;

@Getter @Setter
public final class GUIDRef {
    private final String sourcedId;
    private final GUIDType type;
    private final String href;

    public GUIDRef(GUIDType type, String sourcedId, String href) {
        this.type = type;
        this.sourcedId = sourcedId;
        this.href = href;
    }

    public static GUIDRef of(GUIDType type, String sourcedId) {
        return new GUIDRef(type, sourcedId, null);
    }
}

