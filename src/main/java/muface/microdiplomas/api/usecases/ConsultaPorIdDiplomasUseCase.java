package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCaseById;
import muface.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

@Component
public class ConsultaPorIdDiplomasUseCase extends ArqAbstractUseCaseById<DiplomaDTO, Long> {

    public DiplomaDTO execute(Long idDiploma) {
        return (DiplomaDTO) this.commandService.buscarPorId(idDiploma);
    }


}
