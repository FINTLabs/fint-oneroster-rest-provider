package no.fint.oneroster.util;

import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import lombok.extern.slf4j.Slf4j;
import no.fint.oneroster.exception.BadRequestException;
import no.fint.oneroster.exception.NoSuchFieldException;
import no.fint.oneroster.filter.FilterEvaluator;
import no.fint.oneroster.model.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class OneRosterResponse {

    public static class Builder<T extends Base> {
        private List<T> entities;

        public Builder(List<T> entities) {
            this.entities = entities;
        }

        public Builder<T> sort(Sort sort) {
            sort.get().findFirst().ifPresent(order -> {
                try {
                    entities.sort(new BeanComparator<>(order.getProperty()));
                } catch (Exception e) {
                    entities.sort(new BeanComparator<>("sourcedId"));
                }
            });

            return this;
        }

        public Builder<T> page(Pageable pageable) {
            entities = entities.stream()
                    .skip(pageable.getPageNumber())
                    .limit(pageable.getPageSize())
                    .collect(Collectors.toList());

            return this;
        }

        public Builder<T> filter(ParseTree filter) {
            if (filter == null) {
                return this;
            }

            entities = entities.stream()
                    .filter(entity -> {
                        try {
                            return new FilterEvaluator(entity).visit(filter);
                        } catch (NoSuchFieldException e) {
                            throw new BadRequestException();
                        }

                    }).collect(Collectors.toList());

            return this;
        }

        public List<T> build() {
            return Collections.unmodifiableList(entities);
        }
    }

    public static <T extends Base> PropertyFilter getFieldSelection(Class<T> clazz, String commaDelimitedFields) {
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
