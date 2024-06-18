package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarDiplomasPorNombreDePila extends ArqAbstractUseCase<List<DiplomaDTO>, DiplomaDTO> {


    @Autowired
    public ConsultarDiplomasPorNombreDePila(ArqGenericService commandService) {
        super(commandService);
    }

    public List<DiplomaDTO> execute(DiplomaDTO diplomaDTOFilter) {
        return this.commandService.buscarCoincidenciasNoEstricto(diplomaDTOFilter);
    }


}
