package com.mfc.infra.input.port;

import com.mfc.infra.event.ArqEvent;

public interface ArqCommandEventListenerPort {

    void listen(ArqEvent<?> eventArch);
    void procesarEvento(ArqEvent<?> eventArch);
}
