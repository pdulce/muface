package com.mfc.backend.microdiplomas.api.usecases;

import com.mfc.backend.microdiplomas.api.dto.DiplomaDTOArq;
import com.mfc.backend.microdiplomas.domain.service.DiplomaServicePortArq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActualizarDiplomaUseCase {

    @Autowired
    DiplomaServicePortArq diplomaCommandServicePort;

    public DiplomaDTOArq ejecutar(DiplomaDTOArq diplomaDTO) {
        return diplomaCommandServicePort.actualizar(diplomaDTO);
    }

}
