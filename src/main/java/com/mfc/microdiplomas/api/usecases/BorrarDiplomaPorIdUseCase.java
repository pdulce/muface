package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomaPorIdUseCase extends ArqAbstractUseCase<String, DiplomaDTO>  {

    public BorrarDiplomaPorIdUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    public String execute(DiplomaDTO diplomaDTO) {
        return this.commandService.borrarEntidad(diplomaDTO.getId());
    }

}
