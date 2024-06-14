package com.mfc.microdiplomas.api.usecases;

import com.mfc.microdiplomas.domain.service.DiplomaServicePort;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActualizarDiplomaUseCase {

    @Autowired
    DiplomaServicePort diplomaCommandServicePort;

    public DiplomaDTO ejecutar(DiplomaDTO diplomaDTO) {
        DiplomaDTO diplomaDTOSaved = diplomaCommandServicePort.actualizar(diplomaDTO);

        /*** Business rules ***/
        // TODO: llamar a algun Api Rest al que pasando el nombre del país nos devuelva el continente

        // de momento, esta implementación a modo de ejemplo
        if (diplomaDTOSaved.getRegionOComarca().contains("France")) {
            diplomaDTOSaved.setContinente("Europe");
        } else {
            diplomaDTOSaved.setContinente("Fuera de Europa");
        }
        return diplomaDTOSaved;
    }

}
