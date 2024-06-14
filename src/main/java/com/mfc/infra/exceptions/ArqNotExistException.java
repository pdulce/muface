package com.mfc.infra.exceptions;

public class ArqNotExistException extends Throwable {
    private String msgError;

    public String getMsgError() {
        return msgError;
    }

    public void setMsgError(String msgError) {
        this.msgError = msgError;
    }
}
