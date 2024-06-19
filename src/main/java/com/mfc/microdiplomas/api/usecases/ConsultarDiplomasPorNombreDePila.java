package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarDiplomasPorNombreDePila extends ArqAbstractUseCase<List<DiplomaDTO>, String> {


    public ConsultarDiplomasPorNombreDePila(ArqGenericService commandService) {
        super(commandService);
    }

    public List<DiplomaDTO> execute(String filterName) {
        DiplomaDTO filter = new DiplomaDTO();
        filter.setNombreCompleto(filterName);
        return this.commandService.buscarCoincidenciasNoEstricto(filter);
    }


}
