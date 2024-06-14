package com.mfc.backend.microdiplomas.api.command.acciones;

import com.mfc.backend.microdiplomas.api.dto.DiplomaDTOArq;
import com.mfc.backend.microdiplomas.api.usecases.ActualizarDiplomaUseCase;

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
