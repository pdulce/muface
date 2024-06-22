package com.mfc.infra.event.futuro.publishers;

import com.mfc.infra.event.ArqEvent;

public interface ArqCommandEventPublisherPort {
    void publish(String topic, ArqEvent<?> eventArch);
}
