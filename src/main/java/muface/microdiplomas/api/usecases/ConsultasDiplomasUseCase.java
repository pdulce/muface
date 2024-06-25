package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCaseConsulta;
import muface.microdiplomas.api.dto.DiplomaDTO;
import muface.microdiplomas.domain.service.DiplomaDTOService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsultasDiplomasUseCase extends ArqAbstractUseCaseConsulta<List<DiplomaDTO>, DiplomaDTO> {

    /*public List<DiplomaDTO> execute(Object diplomaDTO) {
        List<DiplomaDTO> diplomaDTOS = new ArrayList<>();
        if (diplomaDTO.getIdCliente() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setIdCliente(diplomaDTO.getIdCliente());
            diplomaDTOS.addAll(this.commandService.buscarCoincidenciasEstricto(filter));
        } else if (diplomaDTO.getNombreCompleto() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setNombreCompleto(diplomaDTO.getNombreCompleto());
            diplomaDTOS.addAll(this.commandService.buscarCoincidenciasNoEstricto(filter));
        } else if (diplomaDTO.getTitulacionDeno() != null) {
            DiplomaDTOService diplomaDtoService = (DiplomaDTOService) this.commandService;
            diplomaDTOS.addAll(diplomaDtoService.buscarDiplomasPorNombreDeTitulacion(diplomaDTO.getTitulacionDeno()));
        } else {
            // si no hay filtro, consultamos todos los registros
            diplomaDTOS.addAll(this.commandService.buscarTodos());
        }
        return diplomaDTOS;
    }*/


    @Override
    public List<DiplomaDTO> execute(DiplomaDTO diplomaDTOFilter) {
        return this.commandService.buscarCoincidenciasNoEstricto(diplomaDTOFilter);
    }


}
