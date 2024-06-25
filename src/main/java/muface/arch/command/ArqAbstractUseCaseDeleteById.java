package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ArqAbstractUseCaseDeleteById<R extends String, Long> implements IArqCommand<R, Long> {

    @Autowired
    protected ArqGenericService commandService;

    public abstract String execute(Long id);


    @Override
    public String executeInner(Object entidadDto) {
        return this.execute((Long)entidadDto);
    }


}

