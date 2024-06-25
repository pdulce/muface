package muface.application.usecases.diplomas;


import jakarta.transaction.Transactional;
import muface.arch.command.ArqAbstractUseCaseDeleteById;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomaPorIdUseCase extends ArqAbstractUseCaseDeleteById<String, Long> {


    @Override
    @Transactional
    public String execute(Long id) {
        return this.commandService.borrarEntidad(id);
    }


}
