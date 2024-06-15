package com.mfc.microdiplomasWithMongo.api.usecases;

import com.mfc.microdiplomasWithMongo.domain.service.DiplomaMServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrarTodosLosDiplomasMUseCase {

    @Autowired
    DiplomaMServicePort diplomaCommandServicePort;

    public void ejecutar() {
        diplomaCommandServicePort.borrar();
    }

}
