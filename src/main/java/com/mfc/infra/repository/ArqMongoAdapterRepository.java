package com.mfc.infra.repository;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Example;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public class ArqMongoAdapterRepository<T, ID> implements ArqPortRepository<T, ID> {

    private MongoRepository<T, ID> mongoRepository;
    private MongoOperations mongoOperations;

    public void setMongoRepository(MongoRepository<?, ?> mongoRepository) {
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

    public String getCollectionName() {
        Class<T> clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
        if (clazz.isAnnotationPresent(Document.class)) {
            Document document = clazz.getAnnotation(Document.class);
            return document.collection();
        } else {
            return "";
        }
    }


}

