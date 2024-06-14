package com.mfc.microdiplomas.domain.service;

import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.infra.output.port.ArqRelationalServicePort;

import java.util.List;

public interface DiplomaServicePort extends ArqRelationalServicePort<Diploma, DiplomaDTO, Long> {

    List<DiplomaDTO> getDiplomasDeLaRegionProvenza();

    void setContinente(DiplomaDTO diplomaDTO);


}
