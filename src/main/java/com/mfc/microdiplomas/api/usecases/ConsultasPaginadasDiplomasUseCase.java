package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCasePagination;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.domain.service.DiplomaDTOService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ConsultasPaginadasDiplomasUseCase extends ArqAbstractUseCasePagination<Page<DiplomaDTO>, DiplomaDTO> {


    public ConsultasPaginadasDiplomasUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    public Page<DiplomaDTO> executeQueryPaginada(DiplomaDTO diplomaDTO, Pageable pageable) {
        if (diplomaDTO.getIdCliente() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setIdCliente(diplomaDTO.getIdCliente());
            return this.commandService.buscarCoincidenciasEstrictoPaginados(filter, pageable);
       } else if (diplomaDTO.getNombreCompleto() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setNombreCompleto(diplomaDTO.getNombreCompleto());
            return this.commandService.buscarCoincidenciasNoEstrictoPaginados(filter, pageable);
        } else if (diplomaDTO.getTitulacionDeno() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setTitulacionDeno(diplomaDTO.getTitulacionDeno());
            DiplomaDTOService diplomaDtoService = (DiplomaDTOService) this.commandService;
            return diplomaDtoService.buscarDiplomasPorNombreDeTitulacion(diplomaDTO.getTitulacionDeno(), pageable);
        } else {
            // si no hay filtro, consultamos todos los registros
           return this.commandService.buscarTodosPaginados(pageable);
        }
    }


}
