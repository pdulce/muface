package com.mfc.microdiplomas.api.facade;

import com.mfc.microdiplomas.api.usecases.ActualizarDiplomaUseCase;
import com.mfc.microdiplomas.api.usecases.BorrarTodosLosDiplomasUseCase;
import com.mfc.microdiplomas.api.usecases.ConsultasDiplomasUseCase;
import com.mfc.microdiplomas.api.dto.DiplomaDTOArq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiplomaFacade {

    @Autowired
    private ActualizarDiplomaUseCase actualizarDiplomaUseCase;

    @Autowired
    private BorrarTodosLosDiplomasUseCase borrarTodosLosDiplomasUseCase;

    @Autowired
    private ConsultasDiplomasUseCase consultasDiplomasUseCase;

    public DiplomaDTOArq actualizarDiploma(DiplomaDTOArq diplomaDTO) {
        return this.actualizarDiplomaUseCase.ejecutar(diplomaDTO);
    }

    public void borrarTodosLosDiplomas() {

        this.borrarTodosLosDiplomasUseCase.ejecutar();
    }

    public List<DiplomaDTOArq> consultarTodosLosDiplomas() {

        return this.consultasDiplomasUseCase.consultarTodos();
    }

    public List<DiplomaDTOArq> consultaDiplomasDeCliente(Long idCustomer) {
        return this.consultasDiplomasUseCase.consultarDiplomasDeCliente(idCustomer);
    }

    public List<DiplomaDTOArq> consultaDiplomasDeClientesConNombre(String name) {
        return this.consultasDiplomasUseCase.consultarDiplomasPorNombreClientes(name);
    }


    public List<DiplomaDTOArq> consultaDiplomasDeRegionProvenza() {
        return this.consultasDiplomasUseCase.getDiplomasDeLaRegionProvenza();
    }


}
