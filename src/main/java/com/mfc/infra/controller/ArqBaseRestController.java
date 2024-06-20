package com.mfc.infra.controller;

import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.usecase.ArqUseCaseExecutor;
import com.mfc.infra.utils.ArqConstantMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;

import java.util.*;

public abstract class ArqBaseRestController {

    Logger logger = LoggerFactory.getLogger(ArqBaseRestController.class);

    @Autowired
    protected ArqUseCaseExecutor useCaseExecutor;
    @Autowired
    protected MessageSource messageSource;

    protected abstract String getBaseUseCasePackage();

    protected final ResponseEntity executeCreateUseCaseWithInputBody(final String useCase, IArqDTO dtoInBody) {
        Object result = useCaseExecutor.executeUseCase(getBaseUseCasePackage().concat(".").
                concat(useCase), dtoInBody);
        Locale locale = LocaleContextHolder.getLocale();
        String successMessage = messageSource.getMessage(ArqConstantMessages.SUCCESS_CREATED, null, locale);
        logger.info(successMessage);
        return ResponseEntity.ok(result);
    }

    protected final ResponseEntity executeUpdateUseCaseWithInputBody(final String useCase, IArqDTO dtoInBody) {
        Object result = useCaseExecutor.executeUseCase(getBaseUseCasePackage().concat(".").
                concat(useCase), dtoInBody);
        Locale locale = LocaleContextHolder.getLocale();
        String successMessage = messageSource.getMessage(ArqConstantMessages.UPDATED_OK, null, locale);
        logger.info(successMessage);
        return ResponseEntity.ok(result);
    }

    protected final ResponseEntity executeDeleteUseCase(final String useCase, Object id) {
        useCaseExecutor.executeUseCase(getBaseUseCasePackage().concat(".").concat(useCase), id);
        Locale locale = LocaleContextHolder.getLocale();
        String successMessage = messageSource.getMessage(id == null ? ArqConstantMessages.DELETED_ALL_OK :
                ArqConstantMessages.DELETED_OK, null, locale);
        logger.info(successMessage);
        return ResponseEntity.ok(successMessage);
    }

    protected final ResponseEntity executeUseQueryCaseWithReqParams(final String useCase, Object param) {
        Object result = useCaseExecutor.executeUseCase(getBaseUseCasePackage().concat(".").
                concat(useCase), param);
        return ResponseEntity.ok(result);
    }

    /*protected String saludar() {
        //logger.info("orchestratorManager charged ? " +  (this.orchestratorManager != null));
        String message = messageSource.getMessage(ArqConstantMessages.GREETING, null, LocaleContextHolder.getLocale());
        logger.info("mapLocales charged ? " +  mapLocales.isEmpty());
        return message == null ? messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND, null,
                getLocale(CASTELLANO)) : message;
    }

    protected String logout() {
        String message = messageSource.getMessage(ArqConstantMessages.LOGOUT, null, LocaleContextHolder.getLocale());
        return message == null ? messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND, null,
                getLocale(CASTELLANO)) : message;
    }*/


}
