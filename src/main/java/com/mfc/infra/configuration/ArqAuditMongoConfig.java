package com.mfc.infra.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(
        basePackages = "com.mfc.infra.event.futuro.repository",
        mongoTemplateRef = "arqAuditMongoTemplate"
)
public class ArqAuditMongoConfig {
    @Bean(name = "arqAuditMongoTemplate")
    public MongoTemplate arqAuditMongoTemplate() {
        MongoProperties mongoProperties = arqAuditMongoProperties();
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create(mongoProperties.getUri()),
                        mongoProperties.getDatabase()
                )
        );
    }

    @Bean(name = "arqAuditMongoProperties")
    @ConfigurationProperties(prefix = "auditoria.data.mongodb")
    public MongoProperties arqAuditMongoProperties() {
        return new MongoProperties();
    }


}
