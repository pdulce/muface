package com.mfc.infra.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArqEvent<T> implements Serializable {

    public static final String EVENT_TOPIC = "topic-command-event";
    public static final String EVENT_TYPE_DELETE = "delete";
    public static final String EVENT_TYPE_CREATE = "create";
    public static final String EVENT_TYPE_UPDATE = "update";

    public static final String STEP_ID_PREFIX = "step-";

    public static final int SAGA_OPE_FAILED = -1;
    public static final int SAGA_OPE_SUCCESS = 200;

    private String id;
    private ArqContextInfo arqContextInfo;
    private ArqSagaStepInfo sagaStepInfo;
    private ArqInnerEvent<T> innerEvent;

    public ArqEvent() {
    }
    public ArqEvent(String almacen, String author, String applicationId,
                    String id, String typeEvent, T data) {
        this.arqContextInfo = new ArqContextInfo(almacen, author, applicationId);
        this.id = id;
        this.innerEvent = new ArqInnerEvent<T>(typeEvent, data);
    }


}
