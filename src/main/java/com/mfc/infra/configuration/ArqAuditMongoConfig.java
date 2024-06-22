package com.mfc.infra.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableConfigurationProperties(ArqAuditMongoConfig.AuditMongoProperties.class)
@EnableMongoRepositories(basePackages = "com.mfc.infra.event.futuro.repository",
        mongoTemplateRef = "arqAuditMongoTemplate")
public class ArqAuditMongoConfig {

    @Bean(name = "arqAuditMongoTemplate")
    public MongoTemplate arqAuditMongoTemplate(AuditMongoProperties auditMongoProperties) {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(MongoClients.
                create(auditMongoProperties.getUri()), auditMongoProperties.getAuthenticationDatabase()));
    }

    @ConfigurationProperties(prefix = "auditoria.data.mongodb")
    public static class AuditMongoProperties extends MongoProperties {
    }

}
