package muface.microdiplomas.api.usecases;

import jakarta.transaction.Transactional;
import muface.arch.command.ArqAbstractUseCaseDeleteList;
import muface.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomasUseCase extends ArqAbstractUseCaseDeleteList<String, DiplomaDTO> {

    @Override
    @Transactional
    public String execute(DiplomaDTO diplomaDTO) {
        return this.commandService.borrarEntidades(diplomaDTO);
    }


}
