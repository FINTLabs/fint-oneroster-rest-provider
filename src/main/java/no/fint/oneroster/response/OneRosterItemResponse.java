package no.fint.oneroster.response;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Getter;
import no.fint.oneroster.model.*;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.Map;

import static no.fint.oneroster.util.ResponseUtil.getPropertyFilter;

@Getter
public class OneRosterItemResponse {
    private final MappingJacksonValue body;

    public OneRosterItemResponse(MappingJacksonValue body) {
        this.body = body;
    }

    public static class Builder<T extends Base> {
        private final T item;

        private SimpleFilterProvider simpleFilterProvider;

        public Builder(T item) {
            this.item = item;
        }

        public Builder<T> fieldSelection(String fields) {
            this.simpleFilterProvider = new SimpleFilterProvider().addFilter("fields", getPropertyFilter(fields, item.getClass()));

            return this;
        }

        public OneRosterItemResponse build() {
            String rel = rels.get(item.getClass());

            MappingJacksonValue body = new MappingJacksonValue(Map.of(rel, item));
            body.setFilters(simpleFilterProvider);

            return new OneRosterItemResponse(body);
        }
    }

    private static final Map<Class<? extends Base>, String> rels = Map.of(
            AcademicSession.class, "academicSession",
            Clazz.class, "class",
            Course.class, "course",
            Enrollment.class, "enrollment",
            Org.class, "org",
            User.class, "user");
}