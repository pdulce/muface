package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class ArqAbstractUseCaseConsulta<R extends List, P> implements IArqCommand<R, P> {

    @Autowired
    protected ArqGenericService commandService;
    public abstract R execute(P params);

    @Override
    public R executeInner(Object entidadDto) {
        return this.execute((P)entidadDto);
    }

}

