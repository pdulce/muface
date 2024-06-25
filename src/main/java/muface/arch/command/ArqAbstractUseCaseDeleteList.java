package muface.arch.command;

import muface.arch.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ArqAbstractUseCaseDeleteList<String, P extends IArqDTO> {

    @Autowired
    protected ArqGenericService commandService;
    public abstract String execute(P filter);


}

