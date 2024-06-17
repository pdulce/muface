package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.usecase.ArqUseCase;
import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultasDiplomasUseCase extends ArqUseCase<Diploma, DiplomaDTO, Long> {

    public List<DiplomaDTO> consultarDiplomasDeCliente(Long customerId) {
        DiplomaDTO diplomaDTOFilter = new DiplomaDTO();
        diplomaDTOFilter.setIdCliente(customerId);
        List<DiplomaDTO> resultados = this.commandService.buscarCoincidenciasEstricto(diplomaDTOFilter);
        return resultados;
    }

    public List<DiplomaDTO> consultarDiplomasPorNombreClientes(String name) {
        DiplomaDTO diplomaDTOFilter = new DiplomaDTO();
        diplomaDTOFilter.setNombreCompleto(name);
        List<DiplomaDTO> resultados = this.commandService.buscarCoincidenciasNoEstricto(diplomaDTOFilter);
        return resultados;
    }

    public List<DiplomaDTO> consultarTodos() {
        return this.commandService.buscarTodos();
    }



}
