package com.mfc.microdiplomasWithMongo.domain.service;

import com.mfc.infra.output.port.ArqServicePort;
import com.mfc.microdiplomasWithMongo.api.dto.DiplomaDTO;
import com.mfc.microdiplomasWithMongo.domain.model.Diploma;

import java.util.List;

public interface DiplomaServicePort extends ArqServicePort<Diploma, DiplomaDTO, String> {

    List<DiplomaDTO> getDiplomasDeLaRegionProvenza();

    void setContinente(DiplomaDTO diplomaDTO);


}
