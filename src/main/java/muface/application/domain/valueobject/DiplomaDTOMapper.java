package muface.application.domain.valueobject;

import muface.arch.command.IArqDTOMapper;
import muface.application.domain.model.Diploma;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class DiplomaDTOMapper implements IArqDTOMapper<Serializable, DiplomaDTO> {
    @Override
    public DiplomaDTO map(Serializable entity) {
        DiplomaDTO diplomaDTO = new DiplomaDTO();
        diplomaDTO.setEntity((Diploma) entity);
        return diplomaDTO;
    }

    @Override
    public DiplomaDTO newInstance() {
        return new DiplomaDTO();
    }

}
