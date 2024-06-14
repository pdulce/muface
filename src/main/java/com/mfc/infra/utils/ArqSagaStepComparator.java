package com.mfc.infra.utils;

import com.mfc.infra.event.ArqEvent;

import java.util.Comparator;

public class ArqSagaStepComparator implements Comparator<Object> {

    @Override
    public int compare(final Object data1, final Object data2) {
        ArqEvent<?> event1 = (ArqEvent<?>) data1;
        ArqEvent<?> event2 = (ArqEvent<?>) data2;
        int retorno = 0;
        if (event1.getSagaStepInfo().getStateOfOperation() > event2.getSagaStepInfo().getStateOfOperation()) {
            retorno = -1;
        } else if (event1.getSagaStepInfo().getStateOfOperation() < event2.getSagaStepInfo().getStateOfOperation()) {
            retorno = 1;
        }
        return retorno;
    }

}
