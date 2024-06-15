package com.mfc.microdiplomasWithMongo.domain.service;

import com.mfc.infra.output.adapter.ArqServiceMongoAdapter;
import com.mfc.microdiplomasWithMongo.api.dto.DiplomaDTO;
import com.mfc.microdiplomasWithMongo.domain.model.DiplomaM;
import com.mfc.microdiplomasWithMongo.domain.repository.DiplomaDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiplomaMServiceAdapter extends ArqServiceMongoAdapter<DiplomaM, DiplomaDTO, String>
        implements DiplomaMServicePort {

    @Autowired
    private DiplomaDocumentRepository repository;

    protected MongoRepository<DiplomaM, String> getRepository() {
        return this.repository;
    }

    @Override
    public List<DiplomaDTO> getDiplomasDeLaRegionProvenza() {
        List<DiplomaDTO> diplomaDTOS = new ArrayList<>();
        DiplomaM dFilter = new DiplomaM();
        dFilter.setRegion("Provenza");
        Example<DiplomaM> example = Example.of(dFilter);
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
