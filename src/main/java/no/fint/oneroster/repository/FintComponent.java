package no.fint.oneroster.repository;

public enum FintComponent {
    EDUCATION("education"),
    ADMINISTRATION("administration");

    private final String key;

    FintComponent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
