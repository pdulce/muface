package com.mfc.infra.repository;

import java.util.List;
import java.util.Optional;

public interface ArqPortRepository<T, ID> {

    T save(T entity);
    void delete(T entity);

    void deleteEntities(List<T> entities);

    void deleteAll();
    Optional<T> findById(ID id);
    List<T> findAll();
    List<T> findByExampleStricted(T example);

    List<T> findByExampleNotStricted(T example);

    String getCollectionName();

}
