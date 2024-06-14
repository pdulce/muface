package com.mfc.infra.input.adapter;

import com.mfc.infra.configuration.ArqConfigProperties;
import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.input.port.ArqCommandEventListenerPort;
import com.mfc.infra.output.port.ArqEventStoreInputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "arch.event-broker-active", havingValue = "true", matchIfMissing = false)
public class ArqArqCommandEventListenerAdapter implements ArqCommandEventListenerPort {

    protected static final String GROUP_ID = "event-adapter";
    Logger logger = LoggerFactory.getLogger(ArqArqCommandEventListenerAdapter.class);

    @Autowired
    ArqConfigProperties arqConfigProperties;

    @Autowired
    ArqEventStoreInputPort eventStoreInputPort;

    @Override
    @KafkaListener(topics = ArqEvent.EVENT_TOPIC, groupId = GROUP_ID)
    public void listen(ArqEvent<?> eventArch) {
        procesarEvento(eventArch);
    }

    @Override
    public void procesarEvento(ArqEvent<?> eventArch) {
        if (!arqConfigProperties.isEventBrokerActive()) {
            logger.error("Debe tener activa la configuración de uso de mensajería en la arquitectura");
            return;
        }
        this.eventStoreInputPort.saveEvent("audit",
                eventArch.getArqContextInfo().getApplicationId(), eventArch.getArqContextInfo().getAlmacen(),
                eventArch.getId(), eventArch);
    }


}
