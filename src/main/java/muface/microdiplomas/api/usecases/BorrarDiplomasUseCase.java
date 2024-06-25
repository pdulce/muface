package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCase;
import muface.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomasUseCase extends ArqAbstractUseCase<String, DiplomaDTO>  {

    @Override
    public String execute(DiplomaDTO diplomaDTO) {
        return this.commandService.borrarEntidades(diplomaDTO);
    }

}
