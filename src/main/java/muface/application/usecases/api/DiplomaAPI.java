package muface.application.usecases.api;

import muface.application.domain.valueobject.DiplomaDTO;
import muface.arch.controller.ArqBaseRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "diplomas")
public class DiplomaAPI extends ArqBaseRestController {

    /**
     * @return
     * Devuelve el valor de la propiedad del application.yml, use-cases:  diplomas   para esta API
     */
    @Override
    protected String getPrefix() {
        return "diplomas";
    }

    /** pesonalized endpoints **/

    @PostMapping("saludar")
    public ResponseEntity<Object> besarMimano(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        return this.executeCreateUseCaseWithInputBody("CasoDeusoNuevoa1211", dtoInBody);
    }

    @PostMapping("crearOtroTipo")
    public ResponseEntity<Object> crear(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        //hacer algo y luego invocar al caso de uso
        return this.executeCreateUseCaseWithInputBody("CrearDiplomaUseCase222", dtoInBody);
    }


}
