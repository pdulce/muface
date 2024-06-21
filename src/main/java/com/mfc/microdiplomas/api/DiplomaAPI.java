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

    // TODO: cómo resolver la problemática de recibir un DTO que tiene información insuficiente
    // TODO: de sus entidades a ser persistidas en Base de datos--> solución, deben mantener en el DTO la información
    // TODO: mínima necesaria para poder crear las entidades hijas de la entidad principal, sean one-to-one o many-to-one
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

    @GetMapping
    public ResponseEntity<Object> consultaPorCampos(@RequestParam(value = "id", required = false) Long id,
                            @RequestParam(value = "clienteId", required = false) Long clienteId,
                            @RequestParam(value = "nombrePila", required = false) String nombrePila,
                            @RequestParam(value = "titulacion", required = false) String titulacion,
                            @RequestParam(required = false) Pageable pageable) {

        DiplomaDTO filter = new DiplomaDTO();
        filter.setId(id);
        filter.setIdCliente(clienteId);
        filter.setNombreCompleto(nombrePila);
        filter.setTitulacionDeno(titulacion);
        if (id != null || pageable == null) {
            return super.executeUseQueryCaseWithReqParams("ConsultasDiplomasUseCase", filter);
        } else {
            return super.executeUseQuerypagCaseWithReqParams("ConsultasPaginadasDiplomasUseCase", filter, pageable);
        }

    }

}
