package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomaPorIdUseCase extends ArqAbstractUseCase<String, Long>  {

    public BorrarDiplomaPorIdUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    public String execute(Long id) {
        return this.commandService.borrarEntidad(id);
    }

}
