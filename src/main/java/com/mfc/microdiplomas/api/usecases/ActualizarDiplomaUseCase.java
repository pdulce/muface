package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

@Component
public class ActualizarDiplomaUseCase extends ArqAbstractUseCase<DiplomaDTO, DiplomaDTO> {


    public ActualizarDiplomaUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    public DiplomaDTO execute(DiplomaDTO diplomaDTO) {
        return (DiplomaDTO) this.commandService.actualizar(diplomaDTO);
    }


}
