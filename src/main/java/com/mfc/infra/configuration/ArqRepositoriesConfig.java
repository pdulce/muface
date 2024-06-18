package com.mfc.infra.configuration;

import com.mfc.infra.repository.ArqMongoAdapterRepository;
import com.mfc.infra.repository.ArqPortRepository;
import com.mfc.infra.repository.ArqRelationalAdapterRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.HashMap;
import java.util.Map;

//@Configuration
public abstract class ArqRepositoriesConfig {

    public abstract Map<Class<?>, Class<?>> getRepositoryJpaMap();

    public abstract Map<Class<?>, Class<?>> getRepositoryMongoMap();

    @Bean
    @Profile("mongo")
    public Map<Class<?>, ArqPortRepository<?, String>> mongoCommonRepositories(MongoOperations mongoOperations) {
        Map<Class<?>, ArqPortRepository<?, String>> repositories = new HashMap<>();
        getRepositoryMongoMap().forEach((entityClass, repositoryClass) -> {
            ArqMongoAdapterRepository<?, String> repository = new ArqMongoAdapterRepository<>(entityClass);
            MongoRepository<?, String> mongoRepository = (MongoRepository<?, String>) getBean(repositoryClass);
            repository.setMongoRepository(mongoRepository);
            repository.setMongoOperations(mongoOperations);
            repositories.put(entityClass, repository);
        });
        return repositories;
    }

    @Bean
    @Profile("jpa")
    public Map<Class<?>, ArqPortRepository<?, Long>> jpaCommonRepositories() {
        Map<Class<?>, ArqPortRepository<?, Long>> repositories = new HashMap<>();
            getRepositoryJpaMap().forEach((entityClass, repositoryClass) -> {
            ArqRelationalAdapterRepository<?, Long> repository = new ArqRelationalAdapterRepository<>(entityClass);
            JpaRepository<?, Long> jpaRepository = (JpaRepository<?, Long>) getBean(repositoryClass);
            repository.setJpaRepository(jpaRepository);
            repositories.put(entityClass, repository);
        });
        return repositories;
    }

    @SuppressWarnings("unchecked")
    private <T> T getBean(Class<T> clazz) {
        return ApplicationContextProvider.getApplicationContext().getBean(clazz);
    }

}





