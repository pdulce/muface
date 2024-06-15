package com.mfc.microdiplomasWithMongo.domain.service;

import com.mfc.infra.output.port.ArqServicePort;
import com.mfc.microdiplomasWithMongo.api.dto.DiplomaDTO;
import com.mfc.microdiplomasWithMongo.domain.model.DiplomaM;

import java.util.List;

public interface DiplomaMServicePort extends ArqServicePort<DiplomaM, DiplomaDTO, String> {

    List<DiplomaDTO> getDiplomasDeLaRegionProvenza();

    void setContinente(DiplomaDTO diplomaDTO);


}
