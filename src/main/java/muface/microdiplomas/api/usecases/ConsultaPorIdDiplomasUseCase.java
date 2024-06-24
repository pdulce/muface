package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCase;
import muface.arch.service.ArqGenericService;
import muface.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

@Component
public class ConsultaPorIdDiplomasUseCase extends ArqAbstractUseCase<DiplomaDTO, Long> {


    public ConsultaPorIdDiplomasUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    public DiplomaDTO execute(Long idDiploma) {
        return (DiplomaDTO) this.commandService.buscarPorId(idDiploma);
    }


}
