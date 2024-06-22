package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomasUseCase extends ArqAbstractUseCase<String, DiplomaDTO>  {

    public BorrarDiplomasUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    public String execute(DiplomaDTO diplomaDTO) {
        return diplomaDTO.getId() == null ? this.commandService.borrarTodos()
                : this.commandService.borrarEntidad(diplomaDTO.getId());
    }

}
