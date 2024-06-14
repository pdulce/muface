package com.mfc.microdiplomas.domain.service;

import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.domain.repository.DiplomaCommandRepository;
import com.mfc.infra.output.adapter.ArqRelationalServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiplomaServiceAdapter extends ArqRelationalServiceAdapter<Diploma, DiplomaDTO, Long>
        implements DiplomaServicePort {

    @Autowired
    private DiplomaCommandRepository repository;

    protected JpaRepository<Diploma, Long> getRepository() {
        return this.repository;
    }

    @Override
    public List<DiplomaDTO> getDiplomasDeLaRegionProvenza() {
        List<DiplomaDTO> diplomaDTOS = new ArrayList<>();
        Diploma dFilter = new Diploma();
        dFilter.setRegion("Provenza");
        Example<Diploma> example = Example.of(dFilter);
        repository.findAll(example).forEach((diploma -> {
            diplomaDTOS.add(DiplomaDTO.convertToDTO(diploma, DiplomaDTO.class));
        }));
        return diplomaDTOS;
    }

    @Override
    public void setContinente(DiplomaDTO diplomaDTO) {
        /*** Business rules ***/
        // TODO: llamar a algun Api Rest al que pasando el nombre del país nos devuelva el continente
        // de momento, esta implementación a modo de ejemplo
        if (diplomaDTO.getRegionOComarca().contains("France")) {
            diplomaDTO.setContinente("Europe");
        } else {
            diplomaDTO.setContinente("Fuera de Europa");
        }
    }

}
