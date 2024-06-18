package com.mfc.microdiplomas.api;

import com.mfc.infra.usecase.ArqUseCaseExecutor;
import com.mfc.infra.usecase.ArqUseCaseParams;
import com.mfc.infra.controller.ArqBaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "diplomas")
public class DiplomaAPI extends ArqBaseRestController {

    private static final String BASE_PACKAGE = "com.mfc.microdiplomas.api.usecases.";
    @Autowired
    ArqUseCaseExecutor useCaseExecutor;

    @PostMapping("execute")
    public ResponseEntity<Object> executeUseCase(@RequestBody ArqUseCaseParams useCaseParams) {
        String useCaseName = useCaseParams.getUseCaseName();
        Object result = useCaseExecutor.executeUseCase(BASE_PACKAGE.concat(useCaseName), useCaseParams.getParams());
        return ResponseEntity.ok(result);
    }


}
