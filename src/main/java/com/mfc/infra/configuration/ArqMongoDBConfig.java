package com.mfc.infra.configuration;

import com.mongodb.client.MongoClients;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;


public class ArqMongoDBConfig {

    @Primary
    @Bean(name = "bdMongoProperties")
    @ConfigurationProperties(prefix = "spring.data.mongodb")
    public MongoProperties bdMongoProperties() {
        return new MongoProperties();
    }

    @Primary
    @Bean(name = "bdMongoTemplate")
    public MongoTemplate bdMongoTemplate(MongoCustomConversions custombdMongoConversions) {
        MongoProperties mongoProperties = bdMongoProperties();
        MongoDatabaseFactory myMongoDbFactory = new SimpleMongoClientDatabaseFactory(
                MongoClients.create(mongoProperties.getUri()), mongoProperties.getDatabase());
        MappingMongoConverter converter = new MappingMongoConverter(myMongoDbFactory, new MongoMappingContext());
        converter.setCustomConversions(custombdMongoConversions);
        converter.afterPropertiesSet();

        return new MongoTemplate(myMongoDbFactory, converter);
    }



}
