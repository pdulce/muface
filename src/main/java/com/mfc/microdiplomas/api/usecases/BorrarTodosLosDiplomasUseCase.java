package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class BorrarTodosLosDiplomasUseCase extends ArqAbstractUseCase<List<DiplomaDTO>, DiplomaDTO>  {

    public BorrarTodosLosDiplomasUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    public List<DiplomaDTO> execute(DiplomaDTO diplomaDTO) {
        if (diplomaDTO.getId() == null) {
            this.commandService.borrarTodos();
        } else {
            this.commandService.borrarEntidad(diplomaDTO.getId());
        }
        return new ArrayList<>();
    }

}
