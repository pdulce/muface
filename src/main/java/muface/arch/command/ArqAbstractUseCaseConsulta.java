package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class ArqAbstractUseCaseConsulta<R extends List, P> {

    @Autowired
    protected ArqGenericService commandService;
    public abstract List execute(P params);


}

