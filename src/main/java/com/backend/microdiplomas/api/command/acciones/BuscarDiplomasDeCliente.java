package com.backend.microdiplomas.api.command.acciones;

import com.backend.microdiplomas.api.usecases.ConsultasDiplomasUseCase;
import com.backend.microdiplomas.api.dto.DiplomaDTOArq;

import java.util.List;

public class BuscarDiplomasDeCliente implements Command<List<DiplomaDTOArq>> {

    private final ConsultasDiplomasUseCase consultasDiplomasUseCase;

    DiplomaDTOArq diplomaDTO;

    public DiplomaDTOArq getDiplomaDTO() {
        return diplomaDTO;
    }

    public BuscarDiplomasDeCliente(ConsultasDiplomasUseCase consultasDiplomasUseCase, DiplomaDTOArq diplomaDTO) {
        this.consultasDiplomasUseCase = consultasDiplomasUseCase;
        this.diplomaDTO = diplomaDTO;
    }

    @Override
    public List<DiplomaDTOArq> execute() {
        return this.consultasDiplomasUseCase.consultarDiplomasDeCliente(getDiplomaDTO().getIdcustomer());
    }
}
