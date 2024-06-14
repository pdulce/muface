package com.mfc.infra.utils;

import java.util.Comparator;

public class ArqIntegerComparator implements Comparator<Integer> {

    @Override
    public int compare(final Integer o1, final Integer o2) {
        int retorno = 0;
        if (o1.intValue() > o2.intValue()) {
            retorno = 1;
        } else if (o1.intValue() < o2.intValue()) {
            retorno = -1;
        }
        return retorno;
    }

}
