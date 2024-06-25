package muface.microdiplomas.api.usecases;


import muface.arch.command.ArqAbstractUseCaseDeleteById;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomaPorIdUseCase extends ArqAbstractUseCaseDeleteById<String, Long> {


    @Override
    public String execute(Long id) {
        return this.commandService.borrarEntidad(id);
    }
}
