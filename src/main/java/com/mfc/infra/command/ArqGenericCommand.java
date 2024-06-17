package com.mfc.infra.command;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ArqGenericCommand<R, P> implements IArqCommand<R, P> {

    @Autowired
    private ApplicationContext applicationContext;

    private final Class<? extends ArqAbstractUseCase<R, P>> useCaseClass;

    public ArqGenericCommand(Class<? extends ArqAbstractUseCase<R, P>> useCaseClass) {
        this.useCaseClass = useCaseClass;
    }

    @Override
    public R execute(P param) {
        //ArqAbstractUseCase<R, P> useCase = applicationContext.getBean(useCaseClass);
        //return useCase.execute(param);
        return null;
    }

}