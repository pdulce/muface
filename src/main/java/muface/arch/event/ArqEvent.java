package muface.arch.event;

import lombok.Data;
import java.io.Serializable;

@Data
public class ArqEvent<T> implements Serializable {

    public static final String TOPIC_AUDITORIAS = "topic-auditorias";
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
    public ArqEvent(String applicationId, String almacen, String typeEvent, String sessionId,
                    String traceId, String id, T data) {
        this.id = id;
        this.arqContextInfo = new ArqContextInfo(applicationId, almacen, sessionId, traceId);
        this.innerEvent = new ArqInnerEvent<>(typeEvent, data);
    }

}
