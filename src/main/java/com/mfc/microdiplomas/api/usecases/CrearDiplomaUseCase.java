package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class CrearDiplomaUseCase extends ArqAbstractUseCase<DiplomaDTO, DiplomaDTO> {

    public CrearDiplomaUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    @Transactional
    public DiplomaDTO execute(DiplomaDTO diplomaDTO) {
        return (DiplomaDTO) this.commandService.insertar(diplomaDTO);
    }

}
