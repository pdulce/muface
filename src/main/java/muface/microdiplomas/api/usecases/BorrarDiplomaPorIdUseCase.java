package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCase;
import muface.arch.service.ArqGenericService;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomaPorIdUseCase extends ArqAbstractUseCase<String, Long>  {

    public BorrarDiplomaPorIdUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    public String execute(Long id) {
        return this.commandService.borrarEntidad(id);
    }

}
