package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.domain.service.DiplomaDTOService;
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
            DiplomaDTO filter = new DiplomaDTO();
            filter.setIdCliente(diplomaDTO.getIdCliente());
            diplomaDTOS.addAll(this.commandService.buscarCoincidenciasEstricto(filter));
        } else if (diplomaDTO.getNombreCompleto() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setNombreCompleto(diplomaDTO.getNombreCompleto());
            diplomaDTOS.addAll(this.commandService.buscarCoincidenciasNoEstricto(filter));
        } else if (diplomaDTO.getTitulacionDeno() != null) {
            DiplomaDTOService diplomaDtoService = (DiplomaDTOService) this.commandService;
            diplomaDTOS.addAll(diplomaDtoService.buscarDiplomasPorNombreDeTitulacion(diplomaDTO.getTitulacionDeno()));
        } else {
            // si no hay filtro, consultamos todos los registros
            diplomaDTOS.addAll(this.commandService.buscarTodos());
        }
        return diplomaDTOS;
    }


}
