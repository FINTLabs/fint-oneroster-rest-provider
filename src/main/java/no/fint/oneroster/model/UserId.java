package no.fint.oneroster.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserId {
    private final String type;
    private final String identifier;

    public UserId(String type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }
}
