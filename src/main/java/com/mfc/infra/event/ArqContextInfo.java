package com.mfc.infra.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArqContextInfo implements Serializable {
    private String almacen;
    //private String author;
    private String traceId;
    private String sessionId;
    private String applicationId;

    public ArqContextInfo(){}

    public ArqContextInfo(String applicationId, String almacen, String sessionId, String traceId) {
        this.sessionId = sessionId;
        this.traceId = traceId;
        this.almacen = almacen;
        this.applicationId = applicationId;
    }
}
