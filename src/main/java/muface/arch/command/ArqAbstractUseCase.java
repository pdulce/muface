package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ArqAbstractUseCase<R extends IArqDTO, P extends IArqDTO> {
    @Autowired
    protected ArqGenericService commandService;

    public abstract R execute(P params);

}

