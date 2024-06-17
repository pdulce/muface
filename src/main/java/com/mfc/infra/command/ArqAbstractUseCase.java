package com.mfc.infra.command;

public abstract class ArqAbstractUseCase<R, P> {
    public abstract R execute(P params);
}

