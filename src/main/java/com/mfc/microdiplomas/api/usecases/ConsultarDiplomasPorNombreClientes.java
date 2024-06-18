package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarDiplomasPorNombreClientes extends ArqAbstractUseCase<List<DiplomaDTO>, DiplomaDTO> {

    @Autowired
    public ConsultarDiplomasPorNombreClientes(ArqGenericService commandService) {
        super.setParamType(DiplomaDTO.class);
        this.commandService = commandService;
        this.commandService.setDtoClass(DiplomaDTO.class);
    }


    public List<DiplomaDTO> execute(DiplomaDTO diplomaDTOFilter) {
        return this.commandService.buscarCoincidenciasNoEstricto(diplomaDTOFilter);
    }


}
