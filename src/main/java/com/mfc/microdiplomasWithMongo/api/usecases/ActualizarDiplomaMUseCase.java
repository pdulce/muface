package com.mfc.microdiplomasWithMongo.api.usecases;

import com.mfc.microdiplomasWithMongo.api.dto.DiplomaDTO;
import com.mfc.microdiplomasWithMongo.domain.service.DiplomaMServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActualizarDiplomaMUseCase {

    @Autowired
    DiplomaMServicePort diplomaCommandServicePort;

    public DiplomaDTO ejecutar(DiplomaDTO diplomaDTO) {
        DiplomaDTO diplomaDTOSaved = this.diplomaCommandServicePort.actualizar(diplomaDTO);
        this.diplomaCommandServicePort.setContinente(diplomaDTOSaved);
        return diplomaDTOSaved;
    }

}
