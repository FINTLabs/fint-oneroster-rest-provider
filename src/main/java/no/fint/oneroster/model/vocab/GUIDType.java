package no.fint.oneroster.model.vocab;

import com.fasterxml.jackson.annotation.JsonValue;

public enum GUIDType {
    ACADEMICSESSION("academicSession"), CLASS("class"), COURSE("course"), ENROLLMENT("enrollment"), GRADINGPERIOD("gradingPeriod"), ORG("org"), STUDENT("student"), TEACHER("teacher"), TERM("term"), USER("user");

    private final String guidType;

    GUIDType(String guidType) {
        this.guidType = guidType;
    }

    @JsonValue
    public String getGuidType() {
        return guidType;
    }
}
