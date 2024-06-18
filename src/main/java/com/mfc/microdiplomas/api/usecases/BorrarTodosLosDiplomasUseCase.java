package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BorrarTodosLosDiplomasUseCase extends ArqAbstractUseCase<DiplomaDTO, DiplomaDTO>  {

    @Autowired
    public BorrarTodosLosDiplomasUseCase(ArqGenericService commandService) {
        super.setParamType(DiplomaDTO.class);
        this.commandService = commandService;
        this.commandService.setDtoClass(DiplomaDTO.class);
    }
    @Override
    public DiplomaDTO execute(DiplomaDTO diplomaDTO) {
        this.commandService.borrarTodos();
        return diplomaDTO;
    }

}
