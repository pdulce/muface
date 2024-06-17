package com.mfc.infra.repository;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;

public class ArqMongoAdapterRepository<T, ID> implements ArqPortRepository<T, ID> {

    private MongoRepository<T, ID> mongoRepository;
    private MongoOperations mongoOperations;

    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public ArqMongoAdapterRepository(Class<T> classZ) {
        this.entityClass = classZ;
    }

    public Class<T> getClassOfEntity() {
        return this.entityClass;
    }

    public void setMongoRepository(MongoRepository<T, ID> mongoRepository) {
        this.mongoRepository = mongoRepository;
    }
    public void setMongoOperations(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public T save(T entity) {
        return mongoRepository.save(entity);
    }

    @Override
    public void delete(T entity) {
        mongoRepository.delete(entity);
    }

    @Override
    public void deleteEntities(List<T> entities) {
        entities.forEach((entity) -> {
            delete(entity);
        });
    }

    @Override
    public void deleteAll() {
        mongoRepository.deleteAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        return mongoRepository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return mongoRepository.findAll();
    }

    @Override
    public List<T> findByExampleStricted(T example) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues();

        // Crear el Example con el matcher configurado
        Example<T> exampleMongo = Example.of(example, matcher);
        return mongoRepository.findAll(exampleMongo);
    }

    @Override
    public List<T> findByExampleNotStricted(T example) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // Realiza búsquedas LIKE %valor%
                .withIgnoreCase(); // Ignorar mayúsculas/minúsculas

        // Crear el Example con el matcher configurado
        Example<T> exampleMongo = Example.of(example, matcher);
        return mongoRepository.findAll(exampleMongo);
    }


}
