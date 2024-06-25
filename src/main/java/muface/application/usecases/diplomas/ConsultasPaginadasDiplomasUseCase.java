package muface.application.usecases.diplomas;

import muface.arch.command.ArqAbstractUseCasePagination;
import muface.application.domain.valueobject.DiplomaDTO;
import muface.application.domain.service.DiplomaDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class ConsultasPaginadasDiplomasUseCase extends ArqAbstractUseCasePagination<Page<DiplomaDTO>, DiplomaDTO> {

    @Autowired
    private DiplomaDTOService diplomaDTOService;

    public Page<DiplomaDTO> execute(DiplomaDTO diplomaDTO, Pageable pageable) {
        if (diplomaDTO.getIdCliente() != null) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setIdCliente(diplomaDTO.getIdCliente());
            return this.diplomaDTOService.buscarCoincidenciasEstrictoPaginados(filter, pageable);
       } else if (diplomaDTO.getNombreCompleto() != null && !diplomaDTO.getNombreCompleto().isEmpty()) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setNombreCompleto(diplomaDTO.getNombreCompleto());
            return this.diplomaDTOService.buscarCoincidenciasNoEstrictoPaginados(filter, pageable);
        } else if (diplomaDTO.getTitulacionDeno() != null && !diplomaDTO.getTitulacionDeno().isEmpty()) {
            DiplomaDTO filter = new DiplomaDTO();
            filter.setTitulacionDeno(diplomaDTO.getTitulacionDeno());
            return this.diplomaDTOService.buscarDiplomasPorNombreDeTitulacion(diplomaDTO.getTitulacionDeno(), pageable);
        } else {
            // si no hay filtro, consultamos todos los registros
           return this.diplomaDTOService.buscarTodosPaginados(pageable);
        }
    }


}
