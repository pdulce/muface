package com.mfc.infra.output.port;

import com.mfc.infra.event.ArqEvent;

public interface ArqCommandEventPublisherPort {
    void publish(String topic, ArqEvent<?> eventArch);
}
