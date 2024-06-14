package com.mfc.infra.input.port;

import com.mfc.infra.event.ArqEvent;

public interface ArqQueryDomainListenerPort<T> {

    void listen(ArqEvent<?> eventArch);

    void procesarEvento(ArqEvent<?> event);


}
