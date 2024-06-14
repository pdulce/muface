package com.backend.microdiplomas.api.command.acciones;

import com.backend.microdiplomas.api.usecases.BorrarTodosLosDiplomasUseCase;
import com.backend.microdiplomas.api.dto.DiplomaDTOArq;

public class BorrarDiplomaCommand implements Command<DiplomaDTOArq> {

    private final BorrarTodosLosDiplomasUseCase borrarTodosLosDiplomasUseCase;

    DiplomaDTOArq diplomaDTO;

    public DiplomaDTOArq getDiplomaDTO() {
        return diplomaDTO;
    }

    public BorrarDiplomaCommand(BorrarTodosLosDiplomasUseCase borrarTodosLosDiplomasUseCase, DiplomaDTOArq diplomaDTO) {
        this.borrarTodosLosDiplomasUseCase = borrarTodosLosDiplomasUseCase;
        this.diplomaDTO = diplomaDTO;
    }

    @Override
    public DiplomaDTOArq execute() {
        this.borrarTodosLosDiplomasUseCase.ejecutar();
        return null;
    }

}
