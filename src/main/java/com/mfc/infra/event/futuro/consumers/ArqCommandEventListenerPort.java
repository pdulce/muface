package com.mfc.infra.event.futuro.consumers;

import com.mfc.infra.event.ArqEvent;

public interface ArqCommandEventListenerPort {

    void listen(ArqEvent<?> eventArch);
    void procesarEvento(ArqEvent<?> eventArch);
}
