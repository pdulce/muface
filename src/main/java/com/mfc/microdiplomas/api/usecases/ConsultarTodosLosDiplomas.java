package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsultarTodosLosDiplomas extends ArqAbstractUseCase<List<DiplomaDTO>, DiplomaDTO> {

    @Autowired
    public ConsultarTodosLosDiplomas(ArqGenericService commandService) {
        super.setParamType(DiplomaDTO.class);
        this.commandService = commandService;
        this.commandService.setDtoClass(DiplomaDTO.class);
    }


    public List<DiplomaDTO> execute(DiplomaDTO all) {
        return this.commandService.buscarTodos();
    }


}
