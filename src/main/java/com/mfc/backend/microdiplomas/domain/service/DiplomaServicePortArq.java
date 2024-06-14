package com.mfc.backend.microdiplomas.domain.service;

import com.mfc.backend.microdiplomas.api.dto.DiplomaDTOArq;
import com.mfc.backend.microdiplomas.domain.model.Diploma;
import com.mfc.infra.output.port.ArqRelationalServicePort;

import java.util.List;

public interface DiplomaServicePortArq extends ArqRelationalServicePort<Diploma, DiplomaDTOArq, Long> {

    List<DiplomaDTOArq> getDiplomasDeLaRegionProvenza();


}
