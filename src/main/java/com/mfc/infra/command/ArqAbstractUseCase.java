package com.mfc.infra.command;

import com.mfc.infra.service.ArqGenericService;

public abstract class ArqAbstractUseCase<R, P> {

    protected ArqGenericService commandService;

    protected Class<P> paramType;

    protected void setParamType(Class<P> paramType) {
        this.paramType = paramType;
    }

    public Class<P> getParamType() {
        return paramType;
    }

    public abstract R execute(P params);
}

