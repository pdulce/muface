package com.mfc.microdiplomas.api.usecases;

import com.mfc.microdiplomas.domain.service.DiplomaServicePort;
import com.mfc.microdiplomas.api.dto.DiplomaDTOArq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsultasDiplomasUseCase {

    @Autowired
    DiplomaServicePort diplomaCommandServicePort;

    public List<DiplomaDTOArq> consultarDiplomasDeCliente(Long customerId) {
        return this.diplomaCommandServicePort.buscarPorCampoValor("customerId", customerId);
    }

    public List<DiplomaDTOArq> consultarDiplomasPorNombreClientes(String name) {
        return this.diplomaCommandServicePort.buscarPorCampoValor("name", name);
    }

    public List<DiplomaDTOArq> consultarTodos() {
        return this.diplomaCommandServicePort.buscarTodos();
    }

    public List<DiplomaDTOArq> getDiplomasDeLaRegionProvenza() {
        return this.diplomaCommandServicePort.getDiplomasDeLaRegionProvenza();
    }


}
