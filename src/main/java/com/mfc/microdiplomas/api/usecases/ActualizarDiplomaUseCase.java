package com.mfc.microdiplomas.api.usecases;

import com.mfc.microdiplomas.domain.service.DiplomaServicePort;
import com.mfc.microdiplomas.api.dto.DiplomaDTOArq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActualizarDiplomaUseCase {

    @Autowired
    DiplomaServicePort diplomaCommandServicePort;

    public DiplomaDTOArq ejecutar(DiplomaDTOArq diplomaDTO) {
        return diplomaCommandServicePort.actualizar(diplomaDTO);
    }

}
