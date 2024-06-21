package com.mfc.microdiplomas.domain.service;

import com.mfc.infra.repository.ArqPortRepository;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DiplomaDTOService extends ArqGenericService<DiplomaDTO, Long> {

    @Override
    public String getRepositoryEntityOfDTO() {
        return DiplomaDTO.getEntityName();
    }

    public DiplomaDTOService(Map<String, ArqPortRepository<?, Long>> repositories) {
        super(repositories);
    }
}
