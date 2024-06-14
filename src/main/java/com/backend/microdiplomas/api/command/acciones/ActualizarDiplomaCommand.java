package com.backend.microdiplomas.api.command.acciones;

import com.backend.microdiplomas.api.dto.DiplomaDTOArq;
import com.backend.microdiplomas.api.usecases.ActualizarDiplomaUseCase;

public class ActualizarDiplomaCommand implements Command<DiplomaDTOArq> {

    private final ActualizarDiplomaUseCase actualizarDiplomaUseCase;

    DiplomaDTOArq diplomaDTO;

    public DiplomaDTOArq getDiplomaDTO() {
        return diplomaDTO;
    }

    public ActualizarDiplomaCommand(ActualizarDiplomaUseCase actualizarDiplomaUseCase, DiplomaDTOArq diplomaDTO) {
        this.actualizarDiplomaUseCase = actualizarDiplomaUseCase;
        this.diplomaDTO = diplomaDTO;
    }

    @Override
    public DiplomaDTOArq execute() {
        return this.actualizarDiplomaUseCase.ejecutar(getDiplomaDTO());
    }

}
