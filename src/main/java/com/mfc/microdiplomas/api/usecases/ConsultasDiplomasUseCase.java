package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsultasDiplomasUseCase extends ArqAbstractUseCase<List<DiplomaDTO>, DiplomaDTO> {


    public ConsultasDiplomasUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    public List<DiplomaDTO> execute(DiplomaDTO diplomaDTO) {
        List<DiplomaDTO> diplomaDTOS = new ArrayList<>();
        if (diplomaDTO.getId() != null) {
            diplomaDTOS.add((DiplomaDTO) this.commandService.buscarPorId(diplomaDTO.getId()));
        } else if (diplomaDTO.getIdCliente() != null) {
            diplomaDTOS.addAll(this.commandService.buscarCoincidenciasEstricto(diplomaDTO));
        } else if (diplomaDTO.getNombreCompleto() != null) {
            diplomaDTOS.addAll(this.commandService.buscarCoincidenciasNoEstricto(diplomaDTO));
        } else {
            // si no hay filtro, consultamos todos los registros
            diplomaDTOS.addAll(this.commandService.buscarTodos());
        }
        return diplomaDTOS;
    }


}
