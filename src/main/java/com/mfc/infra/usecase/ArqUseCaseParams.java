package com.mfc.infra.usecase;


import java.io.Serializable;

public class ArqUseCaseParams {
    private String useCaseName;
    private Serializable params;

    // Getters and setters

    public String getUseCaseName() {
        return useCaseName;
    }

    public void setUseCaseName(String useCaseName) {
        this.useCaseName = useCaseName;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Serializable params) {
        this.params = params;
    }

}
