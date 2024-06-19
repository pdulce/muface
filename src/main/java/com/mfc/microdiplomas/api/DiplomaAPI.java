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
    public ResponseEntity<Object> borrar(@RequestBody Object id) {
        return super.executeDeleteUseCase("BorrarTodosLosDiplomasUseCase", id);
    }

    @GetMapping
    public ResponseEntity<Object> consultaPorNombrePila(@RequestParam(value = "clienteId", required = false)
                                                            String clienteId,
                                                         @RequestParam(value = "nombrePila", required = false)
                                                         String nombrePila) {
        if (clienteId != null) {
            super.executeUseQueryCaseWithReqParams("ConsultarDiplomasDeCliente", clienteId);
        } else if (nombrePila != null) {
            return super.executeUseQueryCaseWithReqParams("ConsultarDiplomasPorNombreDePila", nombrePila);
        }
        // si no hay filtro, consultamos todos los registros
        return super.executeUseQueryCaseWithReqParams("ConsultarTodosLosDiplomas", null);
    }


}
