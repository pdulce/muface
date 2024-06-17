package com.mfc.infra.exceptions;

public class NotExistException extends RuntimeException {

    private String code;
    private Object[] args;

    public NotExistException(String code, Object[] args) {
        this.code = code;
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public Object[] getArgs() {
        return args;
    }

}
