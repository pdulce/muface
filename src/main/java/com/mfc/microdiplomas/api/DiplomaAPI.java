package com.mfc.microdiplomas.api;

import com.mfc.infra.controller.ArqBaseRestController;
import com.mfc.infra.usecase.ArqUseCaseParams;
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

    @PostMapping("execute")
    public ResponseEntity<Object> executeUseCase(@RequestBody ArqUseCaseParams useCaseParams) {
        return super.executeUseCase(useCaseParams);
    }


}
