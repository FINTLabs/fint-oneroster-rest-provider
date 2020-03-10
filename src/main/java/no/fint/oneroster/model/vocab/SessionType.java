package no.fint.oneroster.model.vocab;

import com.fasterxml.jackson.annotation.JsonValue;

public enum SessionType {
    GRADINGPERIOD("gradingPeriod"), SEMESTER("semester"), SCHOOLYEAR("schoolYear"), TERM("term");

    private final String sessionType;

    SessionType(String sessionType) { this.sessionType = sessionType; }

    @JsonValue
    public String getSessionType() { return sessionType; }
}
