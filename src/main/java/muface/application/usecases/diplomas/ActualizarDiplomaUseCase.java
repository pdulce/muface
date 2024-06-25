package muface.application.usecases.diplomas;

import jakarta.transaction.Transactional;
import muface.arch.command.ArqAbstractUseCase;
import muface.application.domain.valueobject.DiplomaDTO;
import org.springframework.stereotype.Component;

@Component
public class ActualizarDiplomaUseCase extends ArqAbstractUseCase<DiplomaDTO, DiplomaDTO> {

    @Override
    @Transactional
    public DiplomaDTO execute(DiplomaDTO diplomaDTO) {
        return (DiplomaDTO) this.commandService.actualizar(diplomaDTO);
    }

}
