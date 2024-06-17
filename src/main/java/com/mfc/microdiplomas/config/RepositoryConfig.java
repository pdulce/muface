package com.mfc.microdiplomas.config;

import com.mfc.infra.repository.ArqMongoAdapterRepository;
import com.mfc.infra.repository.ArqPortRepository;
import com.mfc.infra.repository.ArqRelationalAdapterRepository;
import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.domain.model.DiplomaDocument;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;

@Configuration
public class RepositoryConfig {

    // crear un hashmap de parejas Entity y DTO y se las pasas al constructor de ArqMongoAdapterRepository y ArqRelationalAdapterRepository
    @Bean
    @Profile("mongo")
    public ArqPortRepository<DiplomaDocument, Long> mongoCommonRepository(MongoRepository<DiplomaDocument, Long> mongoRepository,
                                                                            MongoOperations mongoOperations) {
        ArqMongoAdapterRepository<DiplomaDocument, Long> repository = new ArqMongoAdapterRepository<>(DiplomaDocument.class);
        repository.setMongoRepository(mongoRepository);
        repository.setMongoOperations(mongoOperations);
        return repository;
    }

    @Bean
    @Profile("jpa")
    public ArqPortRepository<Diploma, Long> jpaCommonRepository(JpaRepository<Diploma, Long> jpaRepository) {
        ArqRelationalAdapterRepository<Diploma, Long> repository = new ArqRelationalAdapterRepository<>(Diploma.class);
        repository.setJpaRepository(jpaRepository);
        return repository;
    }

    /*** definir el resto de beans de cada entidad/document para Relational Databases o Mongo **/




}




