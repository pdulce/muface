package com.mfc.microdiplomas.domain.repository;

import com.mfc.microdiplomas.domain.model.Diploma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiplomaJPARepository extends JpaRepository<Diploma, Long> {

}
