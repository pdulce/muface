package com.mfc.infra.event.futuro.output;

import com.mfc.infra.event.ArqEvent;

import java.util.List;

public interface ArqEventStoreInputPort {

    void saveEvent(String applicationId, String almacen, String id, ArqEvent<?> eventArch);

    void update(String applicationId, String almacen, String id, String idEntry, ArqEvent<?> eventArch);

    List<Object> findAggregateByAppAndStoreAndAggregateId(String applicationid, String almacen, String id);

    List<Object> findAllByAppAndStore(String applicationid, String almacen);

    List<Object> findAllByApp(String applicationId);

    List<Object> findAll();

}
