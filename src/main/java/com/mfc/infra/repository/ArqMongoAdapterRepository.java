package com.mfc.infra.repository;

import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public class ArqMongoAdapterRepository<T, ID> implements ArqPortRepository<T, ID> {

    private MongoRepository<T, ID> mongoRepository;
    private MongoOperations mongoOperations;

    private String classOfEntity;

    @Override
    public String getClassOfEntity() {
        return this.classOfEntity;
    }
    @Override
    public void setClassOfEntity(String classOfEntity) {
        this.classOfEntity = classOfEntity;
    }

    public void setMongoRepository(MongoRepository<?, ID> mongoRepository) {
        this.mongoRepository = (MongoRepository<T, ID>) mongoRepository;
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
    public List<T> findByIds(List<ID> ids) {
        return mongoRepository.findAllById(ids);
    }

    @Override
    public List<T> findAll() {
        return mongoRepository.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return mongoRepository.findAll(sort);
    }

    @Override
    public Page<T> findAllPaginated(Pageable pageable) {
        return mongoRepository.findAll(pageable);
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
    public Page<T> findByExampleStrictedPaginated(T example, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues();

        // Crear el Example con el matcher configurado
        Example<T> exampleMongo = Example.of(example, matcher);
        return mongoRepository.findAll(exampleMongo, pageable);
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

    @Override
    public Page<T> findByExampleNotStrictedPaginated(T example, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // Realiza búsquedas LIKE %valor%
                .withIgnoreCase(); // Ignorar mayúsculas/minúsculas

        // Crear el Example con el matcher configurado
        Example<T> exampleMongo = Example.of(example, matcher);
        return mongoRepository.findAll(exampleMongo, pageable);
    }

}

