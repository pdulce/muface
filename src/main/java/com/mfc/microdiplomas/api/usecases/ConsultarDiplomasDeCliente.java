package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarDiplomasDeCliente extends ArqAbstractUseCase<List<DiplomaDTO>, Long> {


    public ConsultarDiplomasDeCliente(ArqGenericService commandService) {
        super(commandService);
    }

    public List<DiplomaDTO> execute(Long customerId) {
        DiplomaDTO diplomaDTO = new DiplomaDTO();
        diplomaDTO.setIdCliente(customerId);
        return this.commandService.buscarCoincidenciasEstricto(diplomaDTO);
    }


}
