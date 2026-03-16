package no.fint.oneroster.client;

public enum FintEndpoint {
    SCHOOL("school"),
    PERSON("person"),
    STUDENT("student"),
    TEACHER("teacher"),
    STUDENT_RELATION("student-relation"),
    TEACHING_RELATION("teaching-relation"),
    KLASSE("klasse"),
    KLASSEMEDLEMSKAP("klassemedlemskap"),
    TEACHING_GROUP("teaching-group"),
    TEACHING_GROUP_MEMBERSHIP("teaching-group-membership"),
    CONTACT_TEACHER_GROUP("contact-teacher-group"),
    CONTACT_TEACHER_GROUP_MEMBERSHIP("contact-teacher-group-membership"),
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
