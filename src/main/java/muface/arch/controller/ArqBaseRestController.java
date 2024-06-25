package muface.arch.controller;

import muface.arch.command.IArqDTO;
import muface.arch.command.ArqUseCaseExecutor;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public class ArqBaseRestController {

    @Autowired
    protected ArqUseCaseExecutor useCaseExecutor;

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

    /*protected String saludar() {
        //logger.info("orchestratorManager charged ? " +  (this.orchestratorManager != null));
        String message = messageSource.getMessage(ArqConstantMessages.GREETING, null, LocaleContextHolder.getLocale());
        logger.info("mapLocales charged ? " +  mapLocales.isEmpty());
        return message == null ? messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND, null,
                getLocale(CASTELLANO)) : message;
    }

    protected String logout() {
        String message = messageSource.getMessage(ArqConstantMessages.LOGOUT, null, LocaleContextHolder.getLocale());
        return message == null ? messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND, null,
                getLocale(CASTELLANO)) : message;
    }*/


}
