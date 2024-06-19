package com.mfc.infra.command;

import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;

public abstract class ArqAbstractUseCase<R, P> {

    protected ArqGenericService commandService;
    protected Class<P> paramType;

    public Class<P> getParamType() {
        return paramType;
    }

    public abstract R execute(P params);

    @Autowired
    public ArqAbstractUseCase(ArqGenericService commandService) {
        this.paramType = (Class<P>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        this.commandService = commandService;
        this.commandService.setDtoClass(this.paramType);
    }

}

