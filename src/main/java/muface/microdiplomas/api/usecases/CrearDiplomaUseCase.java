package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCase;
import muface.microdiplomas.api.dto.DiplomaDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class CrearDiplomaUseCase extends ArqAbstractUseCase<DiplomaDTO, DiplomaDTO> {

    @Override
    @Transactional
    public DiplomaDTO execute(DiplomaDTO diplomaDTO) {
        return (DiplomaDTO) this.commandService.insertar(diplomaDTO);
    }

}
