package muface.arch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.converters.PropertyCustomizingConverter;
import java.util.Optional;
import java.util.List;
import org.springdoc.core.customizers.PropertyCustomizer;

@Configuration
public class ArqSpringDocConfiguration {

    @Bean
    public PropertyCustomizingConverter propertyCustomizingConverter(
            Optional<List<PropertyCustomizer>> customize) {
        return new PropertyCustomizingConverter(customize);
    }

}

