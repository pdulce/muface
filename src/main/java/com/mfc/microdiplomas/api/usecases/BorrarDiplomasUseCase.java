package com.mfc.microdiplomas.api.usecases;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class BorrarDiplomasUseCase extends ArqAbstractUseCase<String, DiplomaDTO>  {

    public BorrarDiplomasUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    public String execute(DiplomaDTO diplomaDTO) {
        String msgBorrado = "";
        if (diplomaDTO.getId() == null) {
            msgBorrado = this.commandService.borrarTodos();
        } else {
            msgBorrado = String.valueOf(this.commandService.borrarEntidad(diplomaDTO.getId()));
        }
        return msgBorrado;
    }

}
