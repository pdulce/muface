package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCase;
import muface.arch.service.ArqGenericService;
import muface.microdiplomas.api.dto.DiplomaDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class CrearDiplomaUseCase extends ArqAbstractUseCase<DiplomaDTO, DiplomaDTO> {

    public CrearDiplomaUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    @Transactional
    public DiplomaDTO execute(DiplomaDTO diplomaDTO) {
        return (DiplomaDTO) this.commandService.insertar(diplomaDTO);
    }

}
