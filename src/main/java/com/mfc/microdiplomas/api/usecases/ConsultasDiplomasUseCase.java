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
        List<DiplomaDTO> resultados = this.diplomaCommandServicePort.buscarCoincidenciasEstricto(diplomaDTOFilter);
        resultados.forEach((diplomaDTO -> {
            this.diplomaCommandServicePort.setContinente(diplomaDTO);
        }));
        return resultados;
    }

    public List<DiplomaDTO> consultarDiplomasPorNombreClientes(String name) {
        DiplomaDTO diplomaDTOFilter = new DiplomaDTO();
        diplomaDTOFilter.setNombreCompleto(name);
        List<DiplomaDTO> resultados = this.diplomaCommandServicePort.buscarCoincidenciasNoEstricto(diplomaDTOFilter);
        resultados.forEach((diplomaDTO -> {
            this.diplomaCommandServicePort.setContinente(diplomaDTO);
        }));
        return resultados;
    }

    public List<DiplomaDTO> consultarTodos() {
        return this.diplomaCommandServicePort.buscarTodos();
    }

    public List<DiplomaDTO> getDiplomasDeLaRegionProvenza() {
        List<DiplomaDTO> resultados = this.diplomaCommandServicePort.getDiplomasDeLaRegionProvenza();
        resultados.forEach((diplomaDTO -> {
            this.diplomaCommandServicePort.setContinente(diplomaDTO);
        }));
        return resultados;
    }


}
