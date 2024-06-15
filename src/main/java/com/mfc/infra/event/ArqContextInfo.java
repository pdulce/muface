package com.mfc.infra.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArqContextInfo implements Serializable {
    private String almacen;
    private String author;
    private String applicationId;

    public ArqContextInfo(){}

    public ArqContextInfo(String almacen, String author, String applicationId) {
        this.almacen = almacen;
        this.author = author;
        this.applicationId = applicationId;
    }
}
