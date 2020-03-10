package no.fint.oneroster.util;

import no.fint.model.resource.Link;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public final class LinkUtil {

    private LinkUtil() {
    }

    public static String normalize(Link link) {
        return Optional.ofNullable(link.getHref())
                .map(href -> StringUtils.substringBeforeLast(href, "/").toLowerCase() + "/" +
                        StringUtils.substringAfterLast(href, "/"))
                .orElse(null);
    }
}
