package muface.arch.controller;

import muface.arch.command.IArqDTO;
import muface.arch.command.ArqUseCaseExecutor;
import jakarta.transaction.Transactional;
import muface.application.domain.valueobject.DiplomaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class ArqBaseRestController {
    Logger logger = LoggerFactory.getLogger(ArqBaseRestController.class);
    @Autowired
    protected ApplicationContext applicationContext;

    @Autowired
    protected ArqUseCaseExecutor useCaseExecutor;

    protected abstract String getPrefix();

    protected String getCasoUso(String key) {
        return applicationContext.getEnvironment().getProperty("arch.use-cases." + getPrefix() + "." + key);
    }

    protected String getCasoUsoInsercion() {
        return getCasoUso("insercion");
    }

    protected String getCasoUsoModificacion() {
        return getCasoUso("modificacion");
    }

    protected String getCasoUsoBorrado() {
        return getCasoUso("borrado-general");
    }

    protected String getCasoUsoBorradoPorId() {
        return getCasoUso("borrado-por-id");
    }

    protected String getCasoUsoConsultaPorId() {
        return getCasoUso("consulta-por-id");
    }

    protected String getCasoUsoConsultaGeneral() {
        return getCasoUso("consulta-general");
    }

    protected String getCasoUsoConsultaPaginada() {
        return getCasoUso("consulta-paginada");
    }

    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        return this.executeCreateUseCaseWithInputBody(getCasoUsoInsercion(), dtoInBody);
    }

    @PutMapping
    public ResponseEntity<Object> actualizar(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        return this.executeCreateUseCaseWithInputBody(getCasoUsoModificacion(), dtoInBody);
    }

    @DeleteMapping
    public ResponseEntity<Object> borrarAll() {
        return this.executeCreateUseCaseWithInputBody(getCasoUsoBorrado(), new DiplomaDTO());
    }

    @PostMapping("borrarSeleccion")
    public ResponseEntity<Object> borrarSeleccion(@RequestBody DiplomaDTO dtoInBody) {
        return this.executeCreateUseCaseWithInputBody(getCasoUsoBorrado(), dtoInBody);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> borrarPorId(@PathVariable Long id) {
        return this.executeUseCaseById(getCasoUsoBorradoPorId(), id);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> consultaPorId(@PathVariable Long id) {
        return this.executeUseCaseById(getCasoUsoConsultaPorId(), id);
    }

    @PostMapping("consulta")
    public ResponseEntity<Object> consulta(@RequestBody DiplomaDTO dtoInBody) { // usaríamos la Entidad no el DTO
        return this.executeCreateUseCaseWithInputBody(getCasoUsoConsultaGeneral(), dtoInBody);
    }

    @PostMapping("consulta-paginada")
    public ResponseEntity<Object> consultapaginados(@RequestBody DiplomaDTO dtoInBody, Pageable pageable) {
        return this.executeUseQueryPagination(getCasoUsoConsultaPaginada(), dtoInBody, pageable);
    }

    /**** private methods ****/


    @Transactional
    protected final ResponseEntity executeCreateUseCaseWithInputBody(final String useCase, IArqDTO dtoInBody) {
        Object result = useCaseExecutor.executeUseCase(useCase, dtoInBody);
        return ResponseEntity.ok(result);
    }

    @Transactional
    protected final ResponseEntity executeUseCaseById(final String useCase, Object id) {
        Object result = useCaseExecutor.executeUseCase(useCase, id);
        return ResponseEntity.ok(result);
    }

    protected final ResponseEntity executeUseQueryPagination(final String useCase, Object paramsObject,
                                                             Pageable pageable) {
        Object result = useCaseExecutor.executePaginationUseCase(useCase, paramsObject, pageable);
        return ResponseEntity.ok(result);
    }

    protected final ResponseEntity executeUseCaseWithReqParams(final String useCase, Object[] paramsObject) {
        Object result = useCaseExecutor.executeUseCase(useCase, paramsObject);
        return ResponseEntity.ok(result);
    }


}
