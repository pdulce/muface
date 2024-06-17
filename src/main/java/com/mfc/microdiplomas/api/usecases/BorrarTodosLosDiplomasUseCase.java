package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrarTodosLosDiplomasUseCase {

    @Autowired
    ArqGenericService commandService;
    public void ejecutar() {
        this.commandService.borrarTodos();
    }

}
