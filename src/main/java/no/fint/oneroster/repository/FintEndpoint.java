package no.fint.oneroster.repository;

public enum FintEndpoint {
    SCHOOL("school"),
    PERSON("person"),
    STUDENT("student"),
    TEACHER("teacher"),
    STUDENT_RELATION("student-relation"),
    TEACHING_RELATION("teaching-relation"),
    BASIS_GROUP("basis-group"),
    TEACHING_GROUP("teaching-group"),
    LEVEL("level"),
    SUBJECT("subject"),
    PERSONNEL("personnel");

    private final String key;

    FintEndpoint(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
