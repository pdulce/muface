package com.mfc.microdiplomas.api.usecases;

import com.mfc.microdiplomas.api.usecases.ActualizarDiplomaUseCase;
import com.mfc.microdiplomas.api.usecases.BorrarTodosLosDiplomasUseCase;
import com.mfc.microdiplomas.api.usecases.ConsultasDiplomasUseCase;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.api.usecases.CrearDiplomaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiplomaUseCasesFacade {

    @Autowired
    private CrearDiplomaUseCase crearDiplomaUseCase;
    @Autowired
    private ActualizarDiplomaUseCase actualizarDiplomaUseCase;

    @Autowired
    private BorrarTodosLosDiplomasUseCase borrarTodosLosDiplomasUseCase;

    @Autowired
    private ConsultasDiplomasUseCase consultasDiplomasUseCase;

    public DiplomaDTO crearDiploma(DiplomaDTO diplomaDTO) {
        return this.crearDiplomaUseCase.ejecutar(diplomaDTO);
    }

    public DiplomaDTO actualizarDiploma(DiplomaDTO diplomaDTO) {
        return this.actualizarDiplomaUseCase.ejecutar(diplomaDTO);
    }

    public void borrarTodosLosDiplomas() {

        this.borrarTodosLosDiplomasUseCase.ejecutar();
    }

    public List<DiplomaDTO> consultarTodosLosDiplomas() {

        return this.consultasDiplomasUseCase.consultarTodos();
    }

    public List<DiplomaDTO> consultaDiplomasDeCliente(Long idCustomer) {
        return this.consultasDiplomasUseCase.consultarDiplomasDeCliente(idCustomer);
    }

    public List<DiplomaDTO> consultaDiplomasDeClientesConNombre(String name) {
        return this.consultasDiplomasUseCase.consultarDiplomasPorNombreClientes(name);
    }


    public List<DiplomaDTO> consultaDiplomasDeRegionProvenza() {
        return this.consultasDiplomasUseCase.getDiplomasDeLaRegionProvenza();
    }


}
