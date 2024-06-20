package com.mfc.microdiplomas;

import com.mfc.infra.configuration.ApplicationContextProvider;
import com.mfc.infra.repository.ArqMongoAdapterRepository;
import com.mfc.infra.repository.ArqPortRepository;
import com.mfc.infra.repository.ArqRelationalAdapterRepository;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.api.dto.DiplomaDocumentDTO;
import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.domain.model.DiplomaDocument;
import com.mfc.microdiplomas.domain.repository.DiplomaJPARepository;
import com.mfc.microdiplomas.domain.repository.DiplomaMongoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;

@Configuration
public class RepoConfig {
    @Bean
    @Profile("mongo")
    public ArqPortRepository<DiplomaDocumentDTO, String> mongoDiplomaDTORepository(MongoOperations mongoOperations) {
        ArqMongoAdapterRepository<DiplomaDocumentDTO, String> repository = new ArqMongoAdapterRepository<>(DiplomaDocumentDTO.class);
        MongoRepository<DiplomaDocument, String> mongoRepository = getBean(DiplomaMongoRepository.class);
        repository.setMongoRepository(mongoRepository);
        repository.setMongoOperations(mongoOperations);
        return repository;
    }

    @Bean
    @Profile("jpa")
    public ArqPortRepository<DiplomaDTO, Long> jpaCommonRepositories() {
        ArqRelationalAdapterRepository<DiplomaDTO, Long> repository = new ArqRelationalAdapterRepository<>(DiplomaDTO.class);
        JpaRepository<Diploma, Long> jpaRepository = getBean(DiplomaJPARepository.class);
        repository.setJpaRepository(jpaRepository);
        return repository;
    }

    @SuppressWarnings("unchecked")
    private <T> T getBean(Class<T> clazz) {
        return ApplicationContextProvider.getApplicationContext().getBean(clazz);
    }

}
