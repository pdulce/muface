package com.mfc.infra.output.port;

import com.mfc.infra.event.ArqEvent;

import java.util.List;

public interface ArqEventStoreInputPort {

    void saveEvent(String type, String applicationId, String almacen, String id, ArqEvent<?> eventArch);

    void update(String type, String applicationId, String almacen, String id, String idEntry, ArqEvent<?> eventArch);

    List<Object> findAggregateByAppAndStoreAndAggregateId(String type, String applicationid, String almacen, String id);

    List<Object> findAllByAppAndStore(String type, String applicationid, String almacen);

    List<Object> findAllByApp(String type, String applicationId);

    List<Object> findAllStoreType(String type);

}
