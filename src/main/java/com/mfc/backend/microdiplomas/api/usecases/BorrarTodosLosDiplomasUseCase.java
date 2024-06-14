package com.mfc.backend.microdiplomas.api.usecases;

import com.mfc.backend.microdiplomas.domain.service.DiplomaServicePortArq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrarTodosLosDiplomasUseCase {

    @Autowired
    DiplomaServicePortArq diplomaCommandServicePort;

    public void ejecutar() {
        diplomaCommandServicePort.borrar();
    }

}
