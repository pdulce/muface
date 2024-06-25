package muface.application.usecases.diplomas;

import muface.arch.command.ArqAbstractUseCaseConsulta;
import muface.application.domain.valueobject.DiplomaDTO;
import muface.application.domain.service.DiplomaDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsultasDiplomasUseCase extends ArqAbstractUseCaseConsulta<List<DiplomaDTO>, DiplomaDTO> {

    @Autowired
    private DiplomaDTOService diplomaDTOService;

    @Override
    public List<DiplomaDTO> execute(DiplomaDTO diplomaDTOFilter) {
        List<DiplomaDTO> diplomaDTOS = new ArrayList<>();
        if (diplomaDTOFilter.getIdCliente() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setIdCliente(diplomaDTOFilter.getIdCliente());
            diplomaDTOS.addAll(this.diplomaDTOService.buscarCoincidenciasEstricto(filter));
        } else if (diplomaDTOFilter.getNombreCompleto() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setNombreCompleto(diplomaDTOFilter.getNombreCompleto());
            diplomaDTOS.addAll(this.diplomaDTOService.buscarCoincidenciasNoEstricto(filter));
        } else if (diplomaDTOFilter.getTitulacionDeno() != null) {
            diplomaDTOS.addAll(this.diplomaDTOService.buscarDiplomasPorNombreDeTitulacion(diplomaDTOFilter.getTitulacionDeno()));
        } else {
            // si no hay filtro, consultamos todos los registros
            diplomaDTOS.addAll(this.diplomaDTOService.buscarTodos());
        }
        return diplomaDTOS;
    }


}
