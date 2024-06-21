package com.mfc.microdiplomas.domain.repository;

import com.mfc.microdiplomas.domain.model.Diploma;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiplomaJPARepository extends JpaRepository<Diploma, Long> {

    @Query("SELECT d FROM Diploma d JOIN d.titulacion t WHERE t.name LIKE %:titulacionName%")
    List<Diploma> findDiplomasByTitulacionName(@Param("titulacionName") String titulacionName);
    @Query("SELECT d FROM Diploma d JOIN d.titulacion t WHERE t.name LIKE %:titulacionName%")
    Page<Diploma> findDiplomasByTitulacionName(@Param("titulacionName") String titulacionName, Pageable pageable);

}
