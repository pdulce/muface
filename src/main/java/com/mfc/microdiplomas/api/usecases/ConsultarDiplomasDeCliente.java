package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultarDiplomasDeCliente extends ArqAbstractUseCase<List<DiplomaDTO>, DiplomaDTO> {


    public ConsultarDiplomasDeCliente(ArqGenericService commandService) {
        super(commandService);
    }

    public List<DiplomaDTO> execute(DiplomaDTO diplomaDTO) {
        List<DiplomaDTO> resultados = this.commandService.buscarCoincidenciasEstricto(diplomaDTO);
        return resultados;
    }


}
