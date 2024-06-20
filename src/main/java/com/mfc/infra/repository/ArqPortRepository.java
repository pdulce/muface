package com.mfc.infra.repository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ArqPortRepository<T, ID> {

    Class<T> getClassOfEntity();

    T save(T entity);
    void delete(T entity);

    void deleteEntities(List<T> entities);

    void deleteAll();
    Optional<T> findById(ID id);
    List<T> findAll();

    //List<T> findAll(Pageable pageable);

    List<T> findByExampleStricted(T example);

    List<T> findByExampleNotStricted(T example);

}
