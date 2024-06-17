package com.mfc.infra.usecase;

import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.service.ArqGenericService;
import org.springframework.beans.factory.annotation.Autowired;

public class ArqUseCase<T, D extends IArqDTO, ID> {
    @Autowired
    protected ArqGenericService<T, D, ID> commandService;

}
