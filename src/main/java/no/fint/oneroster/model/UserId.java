package no.fint.oneroster.model;

import lombok.Data;

@Data
public class UserId {
    private final String type;
    private final String identifier;

    public UserId(String type, String identifier) {
        this.type = type;
        this.identifier = identifier;
    }
}
