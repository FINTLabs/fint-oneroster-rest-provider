package no.fint.oneroster.configuration;

import no.fint.oneroster.factory.clazz.ClazzFactory;
import no.fint.oneroster.factory.clazz.DefaultClazzFactory;
import no.fint.oneroster.factory.clazz.MrfylkeClazzFactory;
import no.fint.oneroster.factory.user.DefaultUserFactory;
import no.fint.oneroster.factory.user.NinUserFactory;
import no.fint.oneroster.factory.user.UserFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "oneroster", name = "class-factory", havingValue = "default", matchIfMissing = true)
    public ClazzFactory clazzFactory() {
        return new DefaultClazzFactory();
    }

    @Bean
    @ConditionalOnProperty(prefix = "oneroster", name = "class-factory", havingValue = "mrfylke")
    public ClazzFactory mrfylkeVISClazzFactory() {
        return new MrfylkeClazzFactory();
    }

    @Bean
    @ConditionalOnProperty(prefix = "oneroster", name = "user-factory", havingValue = "default", matchIfMissing = true)
    public UserFactory userFactory() {
        return new DefaultUserFactory();
    }

    @Bean
    @ConditionalOnProperty(prefix = "oneroster", name = "user-factory", havingValue = "nin")
    public UserFactory ninUserFactory() {
        return new NinUserFactory();
    }
}