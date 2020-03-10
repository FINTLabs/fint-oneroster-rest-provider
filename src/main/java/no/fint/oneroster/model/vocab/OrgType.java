package no.fint.oneroster.model.vocab;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrgType {
    DEPARTMENT("department"), SCHOOL("school"), DISTRICT("district"), LOCAL("local"), STATE("state"), NATIONAL("national");

    private final String orgType;

    OrgType(String orgType) { this.orgType = orgType; }

    @JsonValue
    public String getOrgType() { return orgType; }
}