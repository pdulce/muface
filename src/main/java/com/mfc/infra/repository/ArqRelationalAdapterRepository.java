package com.mfc.infra.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;


public class ArqRelationalAdapterRepository<T, ID> implements ArqPortRepository<T, ID> {

    private JpaRepository<T, ID> jpaRepository;
    private String classOfEntity;

    public void setJpaRepository(JpaRepository<?, ID> jpaRepository) {
        this.jpaRepository = (JpaRepository<T, ID>) jpaRepository;
    }

    @Override
    public String getClassOfEntity() {
        return this.classOfEntity;
    }
    @Override
    public void setClassOfEntity(String classOfEntity) {
        this.classOfEntity = classOfEntity;
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

    @Override
    public List<T> findAll(Sort sort) {
        return jpaRepository.findAll(sort);
    }

    @Override
    public Page<T> findAllPaginated(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public List<T> findByExampleStricted(T example) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues();

        // Crear el Example con el matcher configurado
        Example<T> exampleJPA = Example.of(example, matcher);
        return jpaRepository.findAll(exampleJPA);
    }

    @Override
    public Page<T> findByExampleStrictedPaginated(T example, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues();

        // Crear el Example con el matcher configurado
        Example<T> exampleJPA = Example.of(example, matcher);
        return jpaRepository.findAll(exampleJPA, pageable);
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

    @Override
    public Page<T> findByExampleNotStrictedPaginated(T example, Pageable pageable) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // Realiza búsquedas LIKE %valor%
                .withIgnoreCase(); // Ignorar mayúsculas/minúsculas

        // Crear el Example con el matcher configurado
        Example<T> exampleJPA = Example.of(example, matcher);
        return jpaRepository.findAll(exampleJPA, pageable);
    }

}
