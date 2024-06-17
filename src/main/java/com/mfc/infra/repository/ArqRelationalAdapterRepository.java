package com.mfc.infra.repository;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Example;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;


public class ArqRelationalAdapterRepository<T, ID> implements ArqPortRepository<T, ID> {

    private JpaRepository<T, ID> jpaRepository;

    public void setJpaRepository(JpaRepository<?, ?> jpaRepository) {
        this.jpaRepository = (JpaRepository<T, ID>) jpaRepository;
    }

    @Override
    public T save(T entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public void delete(T entity) {
        jpaRepository.delete(entity);
    }

    @Override
    public void deleteEntities(List<T> entities) {
        jpaRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }

    @Override
    public Optional<T> findById(ID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<T> findAll() {
        return jpaRepository.findAll();
    }

    public List<T> findByExampleStricted(T example) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues();

        // Crear el Example con el matcher configurado
        Example<T> exampleJPA = Example.of(example, matcher);
        return jpaRepository.findAll(exampleJPA);
    }


    @Override
    public List<T> findByExampleNotStricted(T example) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // Realiza búsquedas LIKE %valor%
                .withIgnoreCase(); // Ignorar mayúsculas/minúsculas

        // Crear el Example con el matcher configurado
        Example<T> exampleJPA = Example.of(example, matcher);
        return jpaRepository.findAll(exampleJPA);
    }

    public String getCollectionName() {
        return getClassOfEntity().getSimpleName();
    }

    private Class<T> getClassOfEntity() {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
        return entityClass;
    }

}
