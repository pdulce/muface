package com.mfc.infra.output.port;

import com.mfc.infra.event.ArqEvent;

public interface ArqSagaStepPort<T, ID> {

    String getSagaName();

    int getOrderStepInSaga();

    boolean isLastStepInSaga();

    public String getTypeOrOperation();

    Object doSagaOperation(ArqEvent event) throws Throwable;

    Object doSagaCompensation(ArqEvent event) throws Throwable;

}
