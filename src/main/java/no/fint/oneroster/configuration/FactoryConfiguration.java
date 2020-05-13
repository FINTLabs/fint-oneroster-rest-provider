package no.fint.oneroster.configuration;

import no.fint.oneroster.factory.clazz.ClazzFactory;
import no.fint.oneroster.factory.clazz.DefaultClazzFactory;
import no.fint.oneroster.factory.clazz.VTFKClazzFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryConfiguration {

    @Bean
    @ConditionalOnProperty(
            value = "oneroster.profile.clazz-factory",
            havingValue = "default",
            matchIfMissing = true)
    public ClazzFactory clazzFactory() {
        return new DefaultClazzFactory();
    }

    @Bean
    @ConditionalOnProperty(
            value = "oneroster.profile.clazz-factory",
            havingValue = "vtfk")
    public ClazzFactory vtfkClazzFactory() {
        return new VTFKClazzFactory();
    }
}
