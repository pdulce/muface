package com.mfc.infra.event.futuro.output;

import com.mfc.infra.event.ArqEvent;

import java.util.List;

public interface ArqSagaStepInputPort {

    void saveSagaStep(String applicationId, String saga, String id, ArqEvent<?> eventArch);

    void update(String applicationId, String saga, String id, String idEntry, ArqEvent<?> eventArch);

    List<Object> findAggregateByAppAndStoreAndAggregateId(String applicationid, String saga, String id);

    List<Object> findAllByAppAndSaga(String applicationid, String saga);

    List<Object> findAllByApp(String applicationId);

    List<Object> findAll();



}
