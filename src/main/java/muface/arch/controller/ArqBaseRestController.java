package muface.arch.controller;

import muface.arch.command.IArqDTO;
import muface.arch.command.ArqUseCaseExecutor;
import jakarta.transaction.Transactional;
import muface.application.domain.valueobject.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class ArqBaseRestController {

    @Autowired
    protected ArqUseCaseExecutor useCaseExecutor;

    protected abstract String getCasoUsoInsercion();
    protected abstract String getCasoUsoModificacion();
    protected abstract String getCasoUsoBorrado();
    protected abstract String getCasoUsoBorradoPorId();
    protected abstract String getCasoUsoConsultaPorId();
    protected abstract String getCasoUsoConsultaGeneral();
    protected abstract String getCasoUsoConsultaPaginada();

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        return this.executeCreateUseCaseWithInputBody(getCasoUsoInsercion(), dtoInBody);
    }

    @PutMapping
    public ResponseEntity<Object> actualizar(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        return this.executeUpdateUseCaseWithInputBody(getCasoUsoModificacion(), dtoInBody);
    }

    @DeleteMapping
    public ResponseEntity<Object> borrar() {
        return this.executeDeleteUseCase(getCasoUsoBorrado(), new DiplomaDTO());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> borrarPorId(@PathVariable Long id) {
        return this.executeDeleteUseCase(getCasoUsoBorradoPorId(), id);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> consultaPorId(@PathVariable Long id) {
        return this.executeUseQueryCaseWithReqParams(getCasoUsoConsultaPorId(), id);
    }

    /**** Internos ****/


    @Transactional
    protected final ResponseEntity executeCreateUseCaseWithInputBody(final String useCase, IArqDTO dtoInBody) {
        Object result = useCaseExecutor.executeUseCase(useCase, dtoInBody);
        return ResponseEntity.ok(result);
    }

    @Transactional
    protected final ResponseEntity executeUpdateUseCaseWithInputBody(final String useCase, IArqDTO dtoInBody) {
        Object result = useCaseExecutor.executeUseCase(useCase, dtoInBody);
        return ResponseEntity.ok(result);
    }

    @Transactional
    protected final ResponseEntity executeDeleteUseCase(final String useCase, Object id) {
        Object result = useCaseExecutor.executeUseCase(useCase, id);
        return ResponseEntity.ok(result);
    }

    protected final ResponseEntity executeUseQueryCaseWithReqParams(final String useCase, Object param) {
        Object result = useCaseExecutor.executeUseCase(useCase, param);
        return ResponseEntity.ok(result);
    }

    protected final ResponseEntity executeUseQuerypagCaseWithReqParams(final String useCase, Object paramsObject,
                                                                       Pageable pageable) {
        Object result = useCaseExecutor.executePaginationUseCase(useCase, paramsObject, pageable);
        return ResponseEntity.ok(result);
    }


}
