package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrearDiplomaUseCase {

    protected ArqGenericService commandService;

    @Autowired
    public CrearDiplomaUseCase(ArqGenericService commandService) {
        this.commandService = commandService;
        this.commandService.setDtoClass(DiplomaDTO.class);
    }
    public DiplomaDTO ejecutar(DiplomaDTO diplomaDTO) {
        return (DiplomaDTO) this.commandService.crear(diplomaDTO);
    }

}
