package com.mfc.infra.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

public class ArqMongoDBConfig {

    @Primary
    @Bean(name = "bdMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb")
    public MongoProperties bdMongoProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean(name = "bdMongoTemplate")
    public MongoTemplate bdMongoTemplate() {
        MongoProperties mongoProperties = bdMongoProperties();
        return new MongoTemplate(
                new SimpleMongoClientDatabaseFactory(
                        MongoClients.create(mongoProperties.getUri()),
                        mongoProperties.getDatabase()
                )
        );
    }

}
