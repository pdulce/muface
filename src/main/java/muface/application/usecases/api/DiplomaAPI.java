package muface.application.usecases.api;

import muface.arch.controller.ArqBaseRestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "diplomas")
public class DiplomaAPI extends ArqBaseRestController {


    protected String getCasoUsoInsercion(){
        return "CrearDiplomaUseCase";
    }
    protected String getCasoUsoModificacion() {
        return "ActualizarDiplomaUseCase";
    }
    protected String getCasoUsoBorrado() {
        return "BorrarDiplomasUseCase";
    }
    protected String getCasoUsoBorradoPorId() {
        return "BorrarDiplomaPorIdUseCase";
    }
    protected String getCasoUsoConsultaPorId() {
        return "ConsultaPorIdDiplomasUseCase";
    }
    protected String getCasoUsoConsultaGeneral() {
        return "ConsultasDiplomasUseCase";
    }
    protected String getCasoUsoConsultaPaginada() {
        return "ConsultasPaginadasDiplomasUseCase";
    }


}
