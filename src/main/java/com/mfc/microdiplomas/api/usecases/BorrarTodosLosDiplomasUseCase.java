package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrarTodosLosDiplomasUseCase {

    protected ArqGenericService commandService;

    @Autowired
    public BorrarTodosLosDiplomasUseCase(ArqGenericService commandService) {
        this.commandService = commandService;
        this.commandService.setDtoClass(DiplomaDTO.class);
    }
    public void ejecutar() {
        this.commandService.borrarTodos();
    }

}
