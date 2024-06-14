package com.mfc.microdiplomas.api.usecases;

import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.domain.service.DiplomaServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrearDiplomaUseCase {

    @Autowired
    DiplomaServicePort diplomaCommandServicePort;

    public DiplomaDTO ejecutar(DiplomaDTO diplomaDTO) {
        DiplomaDTO diplomaDTOSaved = this.diplomaCommandServicePort.crear(diplomaDTO);
        this.diplomaCommandServicePort.setContinente(diplomaDTOSaved);
        return diplomaDTOSaved;
    }

}
