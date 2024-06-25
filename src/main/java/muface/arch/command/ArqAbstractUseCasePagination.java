package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class ArqAbstractUseCasePagination<R extends Page, P extends IArqDTO> {

    @Autowired
    protected ArqGenericService commandService;
    public abstract R execute(P params, Pageable pageable);


}

