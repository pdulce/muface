package com.mfc.microdiplomas.api.usecases;

import com.mfc.microdiplomas.domain.service.DiplomaServicePort;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultasDiplomasUseCase {

    @Autowired
    DiplomaServicePort diplomaCommandServicePort;

    public List<DiplomaDTO> consultarDiplomasDeCliente(Long customerId) {
        return this.diplomaCommandServicePort.buscarPorCampoValor("customerId", customerId);
    }

    public List<DiplomaDTO> consultarDiplomasPorNombreClientes(String name) {
        return this.diplomaCommandServicePort.buscarPorCampoValor("name", name);
    }

    public List<DiplomaDTO> consultarTodos() {
        return this.diplomaCommandServicePort.buscarTodos();
    }

    public List<DiplomaDTO> getDiplomasDeLaRegionProvenza() {
        return this.diplomaCommandServicePort.getDiplomasDeLaRegionProvenza();
    }


}
