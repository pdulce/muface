package com.mfc.infra.event.futuro.output;

import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.event.ArqEventDocument;

import java.util.List;

public interface ArqEventStoreInputPort {

    void saveEvent(String applicationId, String almacen, String id, ArqEvent<?> eventArch);

    void update(String applicationId, String almacen, String id, String idEntry, ArqEvent<?> eventArch);

    List<ArqEvent<?>> findAggregateByAppAndStoreAndAggregateId(String applicationid, String almacen, String id);

    List<ArqEvent<?>> findAllByAppAndStore(String applicationid, String almacen);

    List<ArqEvent<?>> findAllByApp(String applicationId);

    List<ArqEvent<?>> findAll();

}
