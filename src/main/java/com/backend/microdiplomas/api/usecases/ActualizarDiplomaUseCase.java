package com.backend.microdiplomas.api.usecases;

import com.backend.microdiplomas.domain.service.DiplomaServicePortArq;
import com.backend.microdiplomas.api.dto.DiplomaDTOArq;
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
