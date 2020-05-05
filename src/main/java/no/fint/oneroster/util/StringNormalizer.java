package no.fint.oneroster.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringNormalizer {
    private static final Pattern pattern = Pattern.compile("[^\\p{ASCII}]");

    public static String normalize(String text) {
        return Normalizer
                .normalize(text, Normalizer.Form.NFD)
                .replaceAll(pattern.pattern(), "");
    }
}