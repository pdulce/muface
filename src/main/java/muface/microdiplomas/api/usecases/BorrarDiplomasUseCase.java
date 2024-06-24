package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCase;
import muface.arch.service.ArqGenericService;
import muface.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class BorrarDiplomasUseCase extends ArqAbstractUseCase<String, DiplomaDTO>  {

    public BorrarDiplomasUseCase(ArqGenericService commandService) {
        super(commandService);
    }

    @Override
    public String execute(DiplomaDTO diplomaDTO) {
        List<DiplomaDTO> diplomas2Delete = this.commandService.buscarCoincidenciasEstricto(diplomaDTO);
        return diplomas2Delete.isEmpty() ? this.commandService.borrarTodos()
                : this.commandService.borrarEntidades(diplomas2Delete);
    }

}
