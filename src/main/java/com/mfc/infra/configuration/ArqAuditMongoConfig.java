package com.mfc.infra.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.mfc.infra.event.futuro.repository",
        mongoTemplateRef = "arqAuditMongoTemplate"
)
public class ArqAuditMongoConfig {

    @Bean(name = "arqAuditMongoTemplate")
    public MongoTemplate arqAuditMongoTemplate(MongoCustomConversions customConversions) {
        MongoProperties mongoProperties = arqAuditMongoProperties();
        MongoDatabaseFactory myMongoDbFactory = new SimpleMongoClientDatabaseFactory(
                MongoClients.create(mongoProperties.getUri()), mongoProperties.getDatabase());
        MappingMongoConverter converter = new MappingMongoConverter(myMongoDbFactory, new MongoMappingContext());
        converter.setCustomConversions(customConversions);
        converter.afterPropertiesSet();
        return new MongoTemplate(myMongoDbFactory, converter);
    }

    @Bean(name = "arqAuditMongoProperties")
    @ConfigurationProperties(prefix = "auditoria.data.mongodb")
    public MongoProperties arqAuditMongoProperties() {
        return new MongoProperties();
    }


}

