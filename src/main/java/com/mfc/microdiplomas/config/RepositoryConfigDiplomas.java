package com.mfc.microdiplomas.config;

import com.mfc.infra.configuration.ArqRepositoriesConfig;
import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.domain.model.DiplomaDocument;
import com.mfc.microdiplomas.domain.repository.DiplomaJPARepository;
import com.mfc.microdiplomas.domain.repository.DiplomaMongoRepository;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RepositoryConfigDiplomas extends ArqRepositoriesConfig {


    public Map<Class<?>, Class<?>> getRepositoryJpaMap() {
        Map<Class<?>, Class<?>> mapa = new HashMap<>();
        mapa.put(Diploma.class, DiplomaJPARepository.class);
        return mapa;
    }

    public Map<Class<?>, Class<?>> getRepositoryMongoMap() {
        Map<Class<?>, Class<?>> mapa = new HashMap<>();
        mapa.put(DiplomaDocument.class, DiplomaMongoRepository.class);
        return mapa;
    }

}





