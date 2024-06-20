package com.mfc.infra.command;

import com.mfc.infra.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public abstract class ArqAbstractUseCasePagination<R, P> {

    protected ArqGenericService commandService;
    public abstract R executeQueryPaginada(P params, int page, int size);

    @Autowired
    public ArqAbstractUseCasePagination(ArqGenericService commandService) {
        this.commandService = commandService;
    }

}

