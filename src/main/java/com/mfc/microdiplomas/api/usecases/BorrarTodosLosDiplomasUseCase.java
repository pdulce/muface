package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class BorrarTodosLosDiplomasUseCase extends ArqAbstractUseCase<List<DiplomaDTO>, Long>  {

    public BorrarTodosLosDiplomasUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    public List<DiplomaDTO> execute(Long id) {
        if (id == null) {
            this.commandService.borrarTodos();
        } else {
            this.commandService.borrarEntidad(id);
        }
        return new ArrayList<>();
    }

}
