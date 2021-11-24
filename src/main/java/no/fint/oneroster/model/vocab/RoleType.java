package no.fint.oneroster.model.vocab;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RoleType {
    ADMINISTRATOR("administrator"), AIDE("aide"), GUARDIAN("guardian"), PARENT("parent"), PROCTOR("proctor"), RELATIVE("relative"), STUDENT("student"), TEACHER("teacher");

    private final String roleType;

    RoleType(String roleType) {
        this.roleType = roleType;
    }

    @JsonValue
    public String getRoleType() {
        return roleType;
    }
}
