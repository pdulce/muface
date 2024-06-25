package muface.microdiplomas.api;

import muface.arch.controller.ArqBaseRestController;
import muface.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "diplomas")
public class DiplomaAPI extends ArqBaseRestController {

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        return super.executeCreateUseCaseWithInputBody("CrearDiplomaUseCase", dtoInBody);
    }

    @PutMapping
    public ResponseEntity<Object> actualizar(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        return super.executeUpdateUseCaseWithInputBody("ActualizarDiplomaUseCase", dtoInBody);
    }

    @DeleteMapping
    public ResponseEntity<Object> borrar() {
        return super.executeDeleteUseCase("BorrarDiplomasUseCase", new DiplomaDTO());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> borrarPorId(@PathVariable Long id) {
        return super.executeDeleteUseCase("BorrarDiplomaPorIdUseCase", id);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> consultaPorId(@PathVariable Long id) {
        return super.executeUseQueryCaseWithReqParams("ConsultaPorIdDiplomasUseCase", id);
    }

    @PostMapping("consulta")
    public ResponseEntity<Object> consultaPorCampos(@RequestBody DiplomaDTO filter) {
        return super.executeUseQueryCaseWithReqParams("ConsultasDiplomasUseCase", filter);
    }

    @PostMapping("consultapaginados")
    public ResponseEntity<Object> consultaPaginadaPorCampos(@RequestBody DiplomaDTO filter, Pageable pageable) {
        return super.executeUseQuerypagCaseWithReqParams("ConsultasPaginadasDiplomasUseCase", filter, pageable);
    }

}
