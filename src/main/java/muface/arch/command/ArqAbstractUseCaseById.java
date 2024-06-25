package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ArqAbstractUseCaseById<R extends IArqDTO, Long> {

    @Autowired
    protected ArqGenericService commandService;
    public abstract R execute(Long id);


}

