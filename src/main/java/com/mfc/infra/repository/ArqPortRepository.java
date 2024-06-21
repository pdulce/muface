package com.mfc.infra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    List<T> findAll(Sort sort);

    Page<T> findAllPaginated(Pageable pageable);

    Page<T> findByExampleStrictedPaginated(T example, Pageable pageable);

    List<T> findByExampleStricted(T example);

    Page<T> findByExampleNotStrictedPaginated(T example, Pageable pageable);

    List<T> findByExampleNotStricted(T example);


}
