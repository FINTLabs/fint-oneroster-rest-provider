package no.fint.oneroster.util;

import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
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
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Data
public class OneRosterResponse<T extends Base> {
    private final Class<T> clazz;
    private final String rel;
    private T item;
    private List<T> collection;
    private HttpHeaders headers;
    private MappingJacksonValue body;

    public OneRosterResponse(Class<T> clazz, String rel) {
        this.clazz = clazz;
        this.rel = rel;
    }

    public OneRosterResponse<T> item(T item) {
        this.item = item;

        return this;
    }

    public OneRosterResponse<T> collection(List<T> collection) {
        this.collection = collection;
        this.headers = new HttpHeaders();
        this.headers.set("X-Total-Count", String.valueOf(collection.size()));

        return this;
    }

    public OneRosterResponse<T> pagingAndSorting(Pageable pageable) {
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

    public OneRosterResponse<T> filter(String filter) {
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

    public OneRosterResponse<T> fieldSelection(String fields) {
        body = new MappingJacksonValue(Collections.singletonMap(rel, (item == null ? collection : item)));
        body.setFilters(new SimpleFilterProvider().addFilter("fields", getPropertyFilter(fields)));

        return this;
    }

    public PropertyFilter getPropertyFilter(String commaDelimitedFields) {
        if (commaDelimitedFields == null) {
            return SimpleBeanPropertyFilter.serializeAll();
        }

        if (commaDelimitedFields.isEmpty()) {
            throw new BadRequestException();
        }

        Set<String> fields = StringUtils.commaDelimitedListToSet(StringUtils.trimAllWhitespace(commaDelimitedFields));

        PropertyDescriptor[] propertyDescriptors = PropertyUtils.getPropertyDescriptors(clazz);

        long count = Arrays.stream(propertyDescriptors)
                .map(PropertyDescriptor::getName)
                .filter(fields::contains)
                .count();

        if (fields.size() == count) {
            return SimpleBeanPropertyFilter.filterOutAllExcept(fields);
        }

        return SimpleBeanPropertyFilter.serializeAll();
    }
}
