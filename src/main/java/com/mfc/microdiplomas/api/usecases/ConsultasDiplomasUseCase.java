package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultasDiplomasUseCase {

    protected ArqGenericService commandService;

    @Autowired
    public ConsultasDiplomasUseCase(ArqGenericService commandService) {
        this.commandService = commandService;
        this.commandService.setDtoClass(DiplomaDTO.class);
    }
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
