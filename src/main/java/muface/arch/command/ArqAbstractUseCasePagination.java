package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

public abstract class ArqAbstractUseCasePagination<R, P> {

    protected ArqGenericService commandService;
    public abstract R executeQueryPaginada(P params, Pageable pageable);

    @Autowired
    public ArqAbstractUseCasePagination(ArqGenericService commandService) {
        this.commandService = commandService;
    }

}

