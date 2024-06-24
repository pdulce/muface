package muface.arch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class MongoCustomConversions {

    @Bean
    public org.springframework.data.mongodb.core.convert.MongoCustomConversions customMongoConversions() {
        return new org.springframework.data.mongodb.core.convert.MongoCustomConversions(Arrays.asList(
                new TimestampToDateConverter(),
                new DateToTimestampConverter()
        ));
    }

}
