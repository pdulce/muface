package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ArqAbstractUseCaseDeleteList<R extends String, P extends IArqDTO> implements IArqCommand<R, P> {

    @Autowired
    protected ArqGenericService commandService;
    public abstract R execute(P filter);

    /*public String executeInner(Object entidadDto) {
        return this.execute((P)entidadDto);
    }*/

    @Override
    public R executeInner(P params) {
        return (R) execute((P)params);
    }

}

