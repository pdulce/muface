package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.usecase.ArqUseCase;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.domain.model.Diploma;
import org.springframework.stereotype.Component;

@Component
public class BorrarTodosLosDiplomasUseCase extends ArqUseCase<Diploma, DiplomaDTO, Long> {


    public void ejecutar() {
        this.commandService.borrarTodos();
    }

}
