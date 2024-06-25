package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCaseDeleteList;
import muface.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomasUseCase extends ArqAbstractUseCaseDeleteList<String, DiplomaDTO> {

    @Override
    public String execute(DiplomaDTO diplomaDTO) {
        return this.commandService.borrarEntidades(diplomaDTO);
    }

}
