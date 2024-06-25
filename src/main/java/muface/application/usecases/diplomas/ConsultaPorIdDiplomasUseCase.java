package muface.application.usecases.diplomas;

import muface.arch.command.ArqAbstractUseCaseById;
import muface.application.domain.valueobject.DiplomaDTO;
import org.springframework.stereotype.Component;

@Component
public class ConsultaPorIdDiplomasUseCase extends ArqAbstractUseCaseById<DiplomaDTO, Long> {

    @Override
    public DiplomaDTO execute(Long id) {
        return (DiplomaDTO) this.commandService.buscarPorId(id);
    }

}
