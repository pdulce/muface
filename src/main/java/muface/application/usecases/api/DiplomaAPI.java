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
    public ResponseEntity<Object> besarMimano(@RequestBody DiplomaDTO dtoInBody) {
        return this.executeCreateUseCaseWithInputBody("CasoDeusoNuevoa1211", dtoInBody);
    }
    @PostMapping("creacionNueva")
    public ResponseEntity<Object> crearNovedoso(@RequestBody DiplomaDTO dtoInBody) {
        return this.executeCreateUseCaseWithInputBody("CasoUsoCrearNovedoso", dtoInBody);
    }


}
