package muface.microdiplomas.api.usecases;

import muface.arch.command.ArqAbstractUseCaseConsulta;
import muface.microdiplomas.api.dto.DiplomaDTO;
import muface.microdiplomas.domain.service.DiplomaDTOService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsultasDiplomasUseCase extends ArqAbstractUseCaseConsulta<List<DiplomaDTO>, DiplomaDTO> {

    @Override
    public List<DiplomaDTO> execute(DiplomaDTO diplomaDTOFilter) {
        List<DiplomaDTO> diplomaDTOS = new ArrayList<>();
        if (diplomaDTOFilter.getIdCliente() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setIdCliente(diplomaDTOFilter.getIdCliente());
            diplomaDTOS.addAll(this.commandService.buscarCoincidenciasEstricto(filter));
        } else if (diplomaDTOFilter.getNombreCompleto() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setNombreCompleto(diplomaDTOFilter.getNombreCompleto());
            diplomaDTOS.addAll(this.commandService.buscarCoincidenciasNoEstricto(filter));
        } else if (diplomaDTOFilter.getTitulacionDeno() != null) {
            DiplomaDTOService diplomaDtoService = (DiplomaDTOService) this.commandService;
            diplomaDTOS.addAll(diplomaDtoService.buscarDiplomasPorNombreDeTitulacion(diplomaDTOFilter.getTitulacionDeno()));
        } else {
            // si no hay filtro, consultamos todos los registros
            diplomaDTOS.addAll(this.commandService.buscarTodos());
        }
        return diplomaDTOS;
    }


}
