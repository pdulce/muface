package com.mfc.microdiplomas.domain.service;

import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.api.dto.DiplomaDTOArq;
import com.mfc.infra.output.port.ArqRelationalServicePort;

import java.util.List;

public interface DiplomaServicePort extends ArqRelationalServicePort<Diploma, DiplomaDTOArq, Long> {

    List<DiplomaDTOArq> getDiplomasDeLaRegionProvenza();


}
