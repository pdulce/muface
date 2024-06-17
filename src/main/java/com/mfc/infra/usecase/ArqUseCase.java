package com.mfc.infra.usecase;

import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.service.ArqServicePort;
import org.springframework.beans.factory.annotation.Autowired;

public class ArqUseCase<T, D extends IArqDTO, ID> {
    @Autowired
    protected ArqServicePort<T, D, ID> commandService;

}
