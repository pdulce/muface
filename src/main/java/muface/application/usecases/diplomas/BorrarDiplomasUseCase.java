package muface.application.usecases.diplomas;

import jakarta.transaction.Transactional;
import muface.arch.command.ArqAbstractUseCaseDeleteList;
import muface.application.domain.valueobject.DiplomaDTO;
import org.springframework.stereotype.Component;


@Component
public class BorrarDiplomasUseCase extends ArqAbstractUseCaseDeleteList<String, DiplomaDTO> {

    @Override
    @Transactional
    public String execute(DiplomaDTO diplomaDTO) {
        return this.commandService.borrarEntidades(diplomaDTO);
    }


}
