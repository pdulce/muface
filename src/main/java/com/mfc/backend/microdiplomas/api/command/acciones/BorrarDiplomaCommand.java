package com.mfc.backend.microdiplomas.api.command.acciones;

import com.mfc.backend.microdiplomas.api.dto.DiplomaDTOArq;
import com.mfc.backend.microdiplomas.api.usecases.BorrarTodosLosDiplomasUseCase;

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
