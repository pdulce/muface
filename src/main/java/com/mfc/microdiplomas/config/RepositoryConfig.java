package com.mfc.microdiplomas.config;

import com.mfc.infra.repository.ArqMongoAdapterRepository;
import com.mfc.infra.repository.ArqPortRepository;
import com.mfc.infra.repository.ArqRelationalAdapterRepository;
import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.domain.model.DiplomaDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class RepositoryConfig {

    @Autowired
    private ApplicationContext applicationContext;
    private final List<Class<?>> entityClasses = List.of(
            Diploma.class /*, Entity2.class, Entity3.class, // ...*/
    );

    private final List<Class<?>> entityClassesOfDocument = List.of(
            DiplomaDocument.class /*, Entity2.class, Entity3.class, // ...*/
    );

    @Bean
    @Profile("mongo")
    public Map<Class<?>, ArqPortRepository<?, ?>> mongoRepositories(MongoOperations mongoOperations) {
        Map<Class<?>, ArqPortRepository<?, ?>> repositories = new HashMap<>();
        for (Class<?> entityClass : entityClassesOfDocument) {
            ArqMongoAdapterRepository<?, ?> repository = new ArqMongoAdapterRepository<>();
            repository.setMongoRepository((MongoRepository<?, ?>)
                    applicationContext.getBean(entityClass.getSimpleName() + "MongoRepository"));
            repository.setMongoOperations(mongoOperations);
            repositories.put(entityClass, repository);
        }
        return repositories;
    }

    @Bean
    @Profile("jpa")
    public Map<Class<?>, ArqPortRepository<?, ?>> jpaRepositories() {
        Map<Class<?>, ArqPortRepository<?, ?>> repositories = new HashMap<>();
        for (Class<?> entityClass : entityClasses) {
            ArqRelationalAdapterRepository<?, ?> repository = new ArqRelationalAdapterRepository<>();
            repository.setJpaRepository((JpaRepository<?, ?>)
                    applicationContext.getBean(entityClass.getSimpleName() + "JpaRepository"));
            repositories.put(entityClass, repository);
        }
        return repositories;
    }


}




