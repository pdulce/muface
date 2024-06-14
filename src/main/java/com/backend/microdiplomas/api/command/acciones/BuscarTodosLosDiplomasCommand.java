package com.backend.microdiplomas.api.command.acciones;

import com.backend.microdiplomas.api.dto.DiplomaDTOArq;
import com.backend.microdiplomas.api.usecases.ConsultasDiplomasUseCase;

import java.util.List;

public class BuscarTodosLosDiplomasCommand implements Command<List<DiplomaDTOArq>> {

    private final ConsultasDiplomasUseCase consultasDiplomasUseCase;

    DiplomaDTOArq diplomaDTO;

    public DiplomaDTOArq getDiplomaDTO() {
        return diplomaDTO;
    }

    public BuscarTodosLosDiplomasCommand(ConsultasDiplomasUseCase consultasDiplomasUseCase, DiplomaDTOArq diplomaDTO) {
        this.consultasDiplomasUseCase = consultasDiplomasUseCase;
        this.diplomaDTO = diplomaDTO;
    }

    @Override
    public List<DiplomaDTOArq> execute() {
        return this.consultasDiplomasUseCase.consultarTodos();
    }

}
