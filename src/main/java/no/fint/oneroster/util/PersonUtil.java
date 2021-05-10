package no.fint.oneroster.util;

public final class PersonUtil {

    public static String maskNin(String nin) {
        try {
            return Long.toString((Long.parseLong(nin) / 100), 36);
        } catch (NumberFormatException e) {
            return nin;
        }
    }
}