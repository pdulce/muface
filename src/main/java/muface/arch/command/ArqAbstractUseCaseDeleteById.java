package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ArqAbstractUseCaseDeleteById<String, Long> {

    @Autowired
    protected ArqGenericService commandService;
    public abstract String execute(Long id);


}

