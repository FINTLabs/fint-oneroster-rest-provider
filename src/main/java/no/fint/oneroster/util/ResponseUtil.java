package no.fint.oneroster.util;

import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import no.fint.oneroster.exception.BadRequestException;
import no.fint.oneroster.model.Base;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.Set;

public final class ResponseUtil {

    public static PropertyFilter getPropertyFilter(String commaDelimitedFields, Class<? extends Base> clazz) {
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