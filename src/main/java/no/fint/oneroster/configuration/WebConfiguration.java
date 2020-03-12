package no.fint.oneroster.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import no.fint.oneroster.resolver.FieldResolver;
import no.fint.oneroster.resolver.FilterResolver;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new FieldResolver());
        resolvers.add(new FilterResolver());
    }

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {
        return pageable -> {
            pageable.setPageParameterName("offset");
            pageable.setSizeParameterName("limit");
            pageable.setFallbackPageable(PageRequest.of(0, 100));
        };
    }

    @Bean
    public SortHandlerMethodArgumentResolverCustomizer sortCustomizer() {
        return sort -> sort.setFallbackSort(Sort.by("sourcedId"));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return objectMapper -> objectMapper.serializationInclusion(JsonInclude.Include.NON_NULL);
    }
}

