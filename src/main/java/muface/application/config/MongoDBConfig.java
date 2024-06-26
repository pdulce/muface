package muface.application.config;

import muface.arch.configuration.ArqMongoDBConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.mfc.microdiplomas.domain.repository",
        mongoTemplateRef = "bdMongoTemplate"
)

public class MongoDBConfig extends ArqMongoDBConfig {


}
