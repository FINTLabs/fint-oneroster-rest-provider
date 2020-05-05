package no.fint.oneroster.util;

import java.text.Normalizer;

public class StringNormalizer {

    public static String normalize(String text) {
        return Normalizer
                .normalize(text, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }
}