package com.mfc.microdiplomas.api.usecases;

import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.domain.service.DiplomaServicePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CrearDiplomaUseCase {

    @Autowired
    DiplomaServicePort diplomaCommandServicePort;

    public DiplomaDTO ejecutar(DiplomaDTO diplomaDTO) {
        DiplomaDTO diplomaDTOSaved = diplomaCommandServicePort.crear(diplomaDTO);

        /*** Business rules ***/
        if (diplomaDTOSaved.getRegionOComarca().contains("France")) {
            diplomaDTOSaved.setContinente("Europe");
        } else {
            // llamar a algçun Api Rest al que pasando el nombre del país nos devuelva el continente
            diplomaDTOSaved.setContinente("Fuera de Europa");
        }
        return diplomaDTOSaved;
    }

}
