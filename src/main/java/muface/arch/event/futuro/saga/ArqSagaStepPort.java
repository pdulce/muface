package muface.arch.event.futuro.saga;

import muface.arch.event.ArqEvent;

public interface ArqSagaStepPort<T, ID> {

    String getSagaName();

    int getOrderStepInSaga();

    boolean isLastStepInSaga();

    public String getTypeOrOperation();

    Object doSagaOperation(ArqEvent event) throws Throwable;

    Object doSagaCompensation(ArqEvent event) throws Throwable;

}
