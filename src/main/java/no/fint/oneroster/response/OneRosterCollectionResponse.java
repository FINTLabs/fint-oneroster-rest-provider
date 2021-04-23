package no.fint.oneroster.response;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Getter;
import no.fint.oneroster.antlr.FilterLexer;
import no.fint.oneroster.antlr.FilterParser;
import no.fint.oneroster.exception.BadRequestException;
import no.fint.oneroster.exception.InvalidSyntaxException;
import no.fint.oneroster.exception.NoSuchFieldException;
import no.fint.oneroster.filter.FilterErrorListener;
import no.fint.oneroster.filter.FilterEvaluator;
import no.fint.oneroster.model.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ConsoleErrorListener;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.beanutils.BeanComparator;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static no.fint.oneroster.util.ResponseUtil.getPropertyFilter;

@Getter
public class OneRosterCollectionResponse {
    private final HttpHeaders headers;
    private final MappingJacksonValue body;

    public OneRosterCollectionResponse(HttpHeaders headers, MappingJacksonValue body) {
        this.headers = headers;
        this.body = body;
    }

    public static class Builder<T extends Base> {
        private List<T> collection;
        private final Class<T> clazz;
        private final Integer size;

        private SimpleFilterProvider simpleFilterProvider;

        public Builder(List<T> collection, Class<T> clazz) {
            this.collection = collection;
            this.clazz = clazz;
            this.size = collection.size();
        }

        public Builder<T> filter(String filter) {
            if (filter == null || filter.isEmpty()) {
                return this;
            }

            CharStream stream = CharStreams.fromString(filter.trim());
            FilterLexer lexer = new FilterLexer(stream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            FilterParser parser = new FilterParser(tokens);
            parser.removeErrorListener(ConsoleErrorListener.INSTANCE);
            parser.addErrorListener(FilterErrorListener.INSTANCE);

            ParseTree parseTree;

            try {
                parseTree = parser.logical();
            } catch (InvalidSyntaxException e) {
                return this;
            }

            collection = collection.stream()
                    .filter(entity -> {
                        try {
                            return new FilterEvaluator(entity).visit(parseTree);
                        } catch (NoSuchFieldException e) {
                            throw new BadRequestException();
                        }
                    })
                    .collect(Collectors.toList());

            return this;
        }

        public Builder<T> pagingAndSorting(Pageable pageable) {
            pageable.getSort().get().findFirst().ifPresent(order -> {
                try {
                    collection.sort(new BeanComparator<>(order.getProperty()));
                } catch (Exception ignored) {
                }
            });

            collection = collection.stream()
                    .skip(pageable.getPageNumber())
                    .limit(pageable.getPageSize())
                    .collect(Collectors.toList());

            return this;
        }

        public Builder<T> fieldSelection(String fields) {
            this.simpleFilterProvider = new SimpleFilterProvider().addFilter("fields", getPropertyFilter(fields, clazz));

            return this;
        }

        public OneRosterCollectionResponse build() {
            String rel = rels.get(clazz);

            MappingJacksonValue body = new MappingJacksonValue(Map.of(rel, collection));
            body.setFilters(simpleFilterProvider);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("X-Total-Count", String.valueOf(size));

            return new OneRosterCollectionResponse(httpHeaders, body);
        }
    }

    private static final Map<Class<? extends Base>, String> rels = Map.of(
            AcademicSession.class, "academicSessions",
            Clazz.class, "classes",
            Course.class, "courses",
            Enrollment.class, "enrollments",
            Org.class, "orgs",
            User.class, "users");
}