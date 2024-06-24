package muface.microdiplomas.config;

import muface.arch.configuration.ArqMongoDBConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.mfc.microdiplomas.domain.repository",
        mongoTemplateRef = "bdMongoTemplate"
)
@ConditionalOnProperty(name = "arch.repository-type.active", havingValue = "mongo", matchIfMissing = false)
public class MongoDBConfig extends ArqMongoDBConfig {


}
