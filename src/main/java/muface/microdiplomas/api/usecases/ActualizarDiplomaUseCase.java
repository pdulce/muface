package muface.microdiplomas.api.usecases;

import jakarta.transaction.Transactional;
import muface.arch.command.ArqAbstractUseCase;
import muface.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

@Component
public class ActualizarDiplomaUseCase extends ArqAbstractUseCase<DiplomaDTO, DiplomaDTO> {

    @Override
    @Transactional
    public DiplomaDTO execute(DiplomaDTO diplomaDTO) {
        return (DiplomaDTO) this.commandService.actualizar(diplomaDTO);
    }

}
