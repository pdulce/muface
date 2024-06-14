package com.mfc.microdiplomas.domain.repository;

import com.mfc.microdiplomas.domain.model.Diploma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Esta clase la crearemos solo cuando necesite la capa de negocio de la aplicación nuevas operaciones
 * no disponibles desde la arquitectura, por ejemplo, consultas HQL persionalizas con JOINS etc,
 * consultas filtrando por uno o varios campos específicos de la entidad que maneja este Repository
 */

@Repository
public interface DiplomaCommandRepository extends JpaRepository<Diploma, Long> {



}
