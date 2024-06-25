package muface.microdiplomas.api.dto;

import muface.arch.command.IArqDTOMapper;
import muface.arch.command.IArqEntidad;
import muface.microdiplomas.domain.model.Diploma;
import org.springframework.stereotype.Component;

@Component
public class DiplomaDTOMapper implements IArqDTOMapper<IArqEntidad, DiplomaDTO> {
    @Override
    public DiplomaDTO map(IArqEntidad entity) {
        DiplomaDTO diplomaDTO = new DiplomaDTO();
        diplomaDTO.setEntity((Diploma) entity);
        return diplomaDTO;
    }

    @Override
    public DiplomaDTO newInstance() {
        return new DiplomaDTO();
    }

}
