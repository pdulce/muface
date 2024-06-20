package com.mfc.microdiplomas.api;

import com.mfc.infra.controller.ArqBaseRestController;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Value;
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
    public ResponseEntity<Object> crear(@RequestBody DiplomaDTO dtoInBody) {
        return super.executeCreateUseCaseWithInputBody("CrearDiplomaUseCase", dtoInBody);
    }

    @PutMapping
    public ResponseEntity<Object> actualizar(@RequestBody DiplomaDTO dtoInBody) {
        return super.executeUpdateUseCaseWithInputBody("ActualizarDiplomaUseCase", dtoInBody);
    }

    @DeleteMapping
    public ResponseEntity<Object> borrar(@RequestParam(value = "id", required = false) Long id) {
        return super.executeDeleteUseCase("BorrarTodosLosDiplomasUseCase", id);
    }

    @GetMapping
    public ResponseEntity<Object> consultaPorCampos(@RequestParam(value = "id", required = false) Long id,
                              @RequestParam(value = "clienteId", required = false) Long clienteId,
                              @RequestParam(value = "nombrePila", required = false) String nombrePila) {

        DiplomaDTO filter = new DiplomaDTO();
        filter.setId(id);
        filter.setIdCliente(clienteId);
        filter.setNombreCompleto(nombrePila);
        return super.executeDeleteUseCase("ConsultasDiplomasUseCase", filter);
    }


}
