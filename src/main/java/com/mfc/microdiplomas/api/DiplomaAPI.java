package com.mfc.microdiplomas.api;

import com.mfc.infra.controller.ArqBaseRestController;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "diplomas")
public class DiplomaAPI extends ArqBaseRestController {

    @Value("${arch.api.diplomas.use-case-package}")
    protected String useCasePackage;

    protected String getBaseUseCasePackage() {
        return this.useCasePackage;
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        return super.executeCreateUseCaseWithInputBody("CrearDiplomaUseCase", dtoInBody);
    }

    @PutMapping
    public ResponseEntity<Object> actualizar(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        return super.executeUpdateUseCaseWithInputBody("ActualizarDiplomaUseCase", dtoInBody);
    }


    @DeleteMapping
    public ResponseEntity<Object> borrar(@RequestParam(value = "id", required = false) Long id) {
        DiplomaDTO toDelete = new DiplomaDTO();
        toDelete.setId(id);
        return super.executeDeleteUseCase("BorrarDiplomasUseCase", toDelete);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> consultaPorId(@PathVariable Long id) {
        return super.executeUseQueryCaseWithReqParams("ConsultaPorIdDiplomasUseCase", id);
    }


    @GetMapping
    public ResponseEntity<Object> consultaPorCampos(@RequestParam(value = "clienteId", required = false) Long clienteId,
                                @RequestParam(value = "nombrePila", required = false) String nombrePila,
                                @RequestParam(value = "titulacion", required = false) String titulacion) {

        DiplomaDTO filter = new DiplomaDTO();
        filter.setIdCliente(clienteId);
        filter.setNombreCompleto(nombrePila);
        filter.setTitulacionDeno(titulacion);
        return super.executeUseQueryCaseWithReqParams("ConsultasDiplomasUseCase", filter);
    }

    @GetMapping("paginados")
    public ResponseEntity<Object> consultaPaginadaPorCampos(@RequestParam(value = "clienteId", required = false)
                                                                Long clienteId,
                                @RequestParam(value = "nombrePila", required = false) String nombrePila,
                                @RequestParam(value = "titulacion", required = false) String titulacion,
                                Pageable pageable) {

        DiplomaDTO filter = new DiplomaDTO();
        filter.setIdCliente(clienteId);
        filter.setNombreCompleto(nombrePila);
        filter.setTitulacionDeno(titulacion);
        return super.executeUseQuerypagCaseWithReqParams("ConsultasPaginadasDiplomasUseCase", filter, pageable);
    }

}
