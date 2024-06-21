package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCasePagination;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
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
            return this.commandService.buscarCoincidenciasEstrictoPaginados(diplomaDTO, pageable);
       } else if (diplomaDTO.getNombreCompleto() != null) {
            return this.commandService.buscarCoincidenciasNoEstrictoPaginados(diplomaDTO, pageable);
       } else {
            // si no hay filtro, consultamos todos los registros
           return this.commandService.buscarTodosPaginados(pageable);
        }
    }


}
