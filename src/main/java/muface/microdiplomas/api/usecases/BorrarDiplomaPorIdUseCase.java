package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCase;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomaPorIdUseCase extends ArqAbstractUseCase<String, Long>  {

    @Override
    public String execute(Long id) {
        return this.commandService.borrarEntidad(id);
    }

}
