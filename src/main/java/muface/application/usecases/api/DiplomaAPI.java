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
    public ResponseEntity<Object> saludar(@RequestParam String param1, @RequestParam String param2) {
        return this.executeUseCaseWithReqParams("CasoUsoSaludoEspecial", new Object[]{param1, param2});
    }
    @PostMapping("creacionNueva")
    public ResponseEntity<Object> crearNovedoso(@RequestBody DiplomaDTO dtoInBody) {
        return this.executeCreateUseCaseWithInputBody("CasoUsoCrearNovedoso", dtoInBody);
    }


}
