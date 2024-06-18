package com.mfc.infra.controller;

import com.mfc.infra.configuration.ArqConfigProperties;
import com.mfc.infra.usecase.ArqUseCaseExecutor;
import com.mfc.infra.usecase.ArqUseCaseParams;
import com.mfc.infra.utils.ArqConstantMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

public abstract class ArqBaseRestController {

    Logger logger = LoggerFactory.getLogger(ArqBaseRestController.class);

    @Autowired
    ArqUseCaseExecutor useCaseExecutor;
    @Autowired
    protected MessageSource messageSource;

    protected String uriBase;

    public static final String EUSKERA = "euskera";
    public static final String GALLEGO = "gallego";
    public static final String CATALAN = "catalan";
    public static final String CASTELLANO = "castellano";
    public static final String ENGLISH = "english";
    public final Map<String, Locale> mapLocales = new HashMap<>();

    protected Locale getLocale(String idioma) {
        if (mapLocales.isEmpty()) {
            mapLocales.put(EUSKERA, new Locale("eu", "ES"));
            mapLocales.put(GALLEGO, new Locale("gl", "ES"));
            mapLocales.put(CATALAN, new Locale("ca", "ES"));
            mapLocales.put(CASTELLANO, new Locale("es"));
            mapLocales.put(ENGLISH, new Locale("en"));
        }
        Locale locale = mapLocales.get(idioma);
        return locale == null ? Locale.getDefault() : locale;
    }

    protected abstract String getBaseUseCasePackage();

    public ResponseEntity<Object> executeUseCase(@RequestBody ArqUseCaseParams useCaseParams) {
        String useCaseName = useCaseParams.getUseCaseName();
        Object result = useCaseExecutor.executeUseCase(getBaseUseCasePackage().concat(".").concat(useCaseName),
                useCaseParams.getParams());
        return ResponseEntity.ok(result);
    }

    public String saludar() {
        //logger.info("orchestratorManager charged ? " +  (this.orchestratorManager != null));
        String message = messageSource.getMessage(ArqConstantMessages.GREETING, null, getLocale(CASTELLANO));
        logger.info("mapLocales charged ? " +  mapLocales.isEmpty());
        return message == null ? messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND, null,
                getLocale(CASTELLANO)) : message;
    }

    public String logout() {
        String message = messageSource.getMessage(ArqConstantMessages.LOGOUT, null, getLocale(CASTELLANO));
        return message == null ? messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND, null,
                getLocale(CASTELLANO)) : message;
    }


}
