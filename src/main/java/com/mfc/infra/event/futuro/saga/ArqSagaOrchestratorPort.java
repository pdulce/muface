package com.mfc.infra.event.futuro.saga;

import com.mfc.infra.event.ArqEvent;

public interface ArqSagaOrchestratorPort<T> {

    void listen(ArqEvent<?> event);
    ArqEvent startSaga(String sagaName, T data);

    String[] getLastStateOfTansactionInSaga(String applicationId, String saganame, String transaccId);

    String DO_OPERATION = "exec-ope-";
    String SAGA_FROM_STEP_TOPIC = "saga-from-step-topic";

}
