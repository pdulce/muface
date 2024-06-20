package com.mfc.infra.command;

import com.mfc.infra.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ArqAbstractUseCase<R, P> {

    protected ArqGenericService commandService;
    public abstract R execute(P params);

    @Autowired
    public ArqAbstractUseCase(ArqGenericService commandService) {
        this.commandService = commandService;
    }

}

