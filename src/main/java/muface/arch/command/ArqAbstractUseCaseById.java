package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ArqAbstractUseCaseById<R extends IArqDTO, Long> implements IArqCommand<R, Long> {

    @Autowired
    protected ArqGenericService commandService;
    public abstract R execute(Long id);


    @Override
    public R executeInner(Object entidadDto) {
        return this.execute((Long)entidadDto);
    }


}

