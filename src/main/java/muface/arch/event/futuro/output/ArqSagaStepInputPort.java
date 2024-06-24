package muface.arch.event.futuro.output;

import muface.arch.event.ArqEvent;

import java.util.List;

public interface ArqSagaStepInputPort {

    void saveSagaStep(String applicationId, String saga, String id, ArqEvent<?> eventArch);

    void update(String applicationId, String saga, String id, String idEntry, ArqEvent<?> eventArch);

    List<ArqEvent<?>> findAggregateByAppAndSagaAndAggregateId(String applicationid, String saga, String id);

    List<ArqEvent<?>> findAllByAppAndSaga(String applicationid, String saga);

    List<ArqEvent<?>> findAllByApp(String applicationId);

    List<ArqEvent<?>> findAll();



}
