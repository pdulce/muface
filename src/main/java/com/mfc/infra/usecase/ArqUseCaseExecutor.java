package com.mfc.infra.usecase;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.exceptions.ArqBussinessRuleException;
import com.mfc.infra.utils.ArqConversionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class ArqUseCaseExecutor {

    @Autowired
    ApplicationContext applicationContext;

    public <R, P> R executeUseCase(String useCaseName, P paramsMap) {
        try {
            Class<?> useCaseClass = Class.forName(useCaseName);
            ArqAbstractUseCase<R, P> useCase = (ArqAbstractUseCase<R, P>) applicationContext.getBean(useCaseClass);
            String jsonParams = ArqConversionUtils.map2Jsonstring((LinkedHashMap<String, Object>) paramsMap);
            P params = ArqConversionUtils.jsonStringToObject(jsonParams, useCase.getParamType());

            return useCase.execute(params);

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);

        } catch (Throwable exc) {

            throw new ArqBussinessRuleException(exc.getMessage(), null);
        }
    }
}