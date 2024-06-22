package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

@Component
public class ConsultaPorIdDiplomasUseCase extends ArqAbstractUseCase<DiplomaDTO, Long> {


    public ConsultaPorIdDiplomasUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    public DiplomaDTO execute(Long idDiploma) {
        return (DiplomaDTO) this.commandService.buscarPorId(idDiploma);
    }


}
