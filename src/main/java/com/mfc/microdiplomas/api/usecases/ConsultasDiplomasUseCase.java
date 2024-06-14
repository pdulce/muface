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
        DiplomaDTO diplomaDTOFilter = new DiplomaDTO();
        diplomaDTOFilter.setIdCliente(customerId);
        return this.diplomaCommandServicePort.buscarPorCampoValor(diplomaDTOFilter);
    }

    public List<DiplomaDTO> consultarDiplomasPorNombreClientes(String name) {
        DiplomaDTO diplomaDTOFilter = new DiplomaDTO();
        diplomaDTOFilter.setNombreCompleto(name);
        return this.diplomaCommandServicePort.buscarPorCampoValor(diplomaDTOFilter);
    }

    public List<DiplomaDTO> consultarTodos() {
        return this.diplomaCommandServicePort.buscarTodos();
    }

    public List<DiplomaDTO> getDiplomasDeLaRegionProvenza() {
        return this.diplomaCommandServicePort.getDiplomasDeLaRegionProvenza();
    }


}
