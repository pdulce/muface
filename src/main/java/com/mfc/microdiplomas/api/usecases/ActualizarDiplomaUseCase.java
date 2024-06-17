package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.domain.model.Diploma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActualizarDiplomaUseCase {

    protected ArqGenericService commandService;

    @Autowired
    public ActualizarDiplomaUseCase(ArqGenericService commandService) {
        this.commandService = commandService;
        this.commandService.setDtoClass(DiplomaDTO.class);
    }

    public DiplomaDTO ejecutar(DiplomaDTO diplomaDTO) {
        return (DiplomaDTO) this.commandService.actualizar(diplomaDTO);
    }

}
