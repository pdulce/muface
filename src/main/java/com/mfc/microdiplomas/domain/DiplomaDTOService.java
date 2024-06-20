package com.mfc.microdiplomas.domain;

import com.mfc.infra.repository.ArqPortRepository;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.domain.model.Diploma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DiplomaDTOService extends ArqGenericService<DiplomaDTO, Long> {

    @Autowired
    protected ArqPortRepository<Diploma, Long> myRepoDiplomaDTO;

    @Override
    protected ArqPortRepository<Diploma, Long> getRepository() {
        return this.myRepoDiplomaDTO;
    }
}
