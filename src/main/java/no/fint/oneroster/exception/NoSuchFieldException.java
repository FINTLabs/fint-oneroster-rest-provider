package no.fint.oneroster.exception;

public class NoSuchFieldException extends RuntimeException {

    public NoSuchFieldException() {

    }

    public NoSuchFieldException(String message) {
        super(message);
    }
}
