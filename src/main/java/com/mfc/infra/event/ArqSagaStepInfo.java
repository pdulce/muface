package com.mfc.infra.event;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArqSagaStepInfo implements Serializable {
    private String sagaName;
    private Long transactionIdentifier;
    private int stepNumber;
    private int nextStepNumberToProccess;
    private int stateOfOperation;
    private int stateOfCompensation;
    private boolean doCompensateOp;
    private boolean lastStep;
    private String errorMsgOperation;
    private String errorMsgCompensation;

    private ArqSagaStepInfo(){}

    public ArqSagaStepInfo(String sagaName, Integer nextStepNumberToProccess, Long transactionIdentifier){
        this.sagaName = sagaName;
        this.nextStepNumberToProccess = nextStepNumberToProccess;
        this.stateOfOperation = 0; // este valor es que aún no se ha ejecutado esta operación
        this.stateOfCompensation = 0; // este valor es que aún no se ha ejecutado esta operación
        this.transactionIdentifier = transactionIdentifier;
        this.doCompensateOp = false;
        this.lastStep = false;
    }


}
