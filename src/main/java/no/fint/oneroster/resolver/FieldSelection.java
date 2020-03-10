package no.fint.oneroster.resolver;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Set;

public class FieldSelection implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return FilterProvider.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Set<String> fields = StringUtils.commaDelimitedListToSet(StringUtils.trimAllWhitespace(webRequest.getParameter("fields")));

        SimpleBeanPropertyFilter propertyFilter = fields.isEmpty() ? SimpleBeanPropertyFilter.serializeAll() : SimpleBeanPropertyFilter.filterOutAllExcept(fields);

        return new SimpleFilterProvider()
                .setFailOnUnknownId(false)
                .addFilter("fields", propertyFilter);
    }
}
