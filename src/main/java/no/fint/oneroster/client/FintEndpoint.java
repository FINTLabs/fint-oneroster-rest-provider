package no.fint.oneroster.client;

public enum FintEndpoint {
    SCHOOL("school"),
    PERSON("person"),
    STUDENT("student"),
    TEACHER("teacher"),
    STUDENT_RELATION("student-relation"),
    TEACHING_RELATION("teaching-relation"),
    BASIS_GROUP("basis-group"),
    TEACHING_GROUP("teaching-group"),
    CONTACT_TEACHER_GROUP("contact-teacher-group"),
    LEVEL("level"),
    SUBJECT("subject"),
    PERSONNEL("personnel"),
    TERM("term"),
    SCHOOL_YEAR("school-year");

    private final String key;

    FintEndpoint(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
