package muface.arch.event.futuro.consumers;

import muface.arch.configuration.ArqConfigProperties;
import muface.arch.event.ArqEvent;
import muface.arch.event.futuro.output.ArqEventStoreInputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "arch.event-broker-active", havingValue = "true", matchIfMissing = false)
public class ArqCommandEventListenerAdapter implements ArqCommandEventListenerPort {

    protected static final String GROUP_ID = "event-adapter";
    Logger logger = LoggerFactory.getLogger(ArqCommandEventListenerAdapter.class);

    @Autowired
    ArqConfigProperties arqConfigProperties;

    @Autowired
    ArqEventStoreInputPort eventStoreInputPort;

    @Override
    @KafkaListener(topics = ArqEvent.TOPIC_AUDITORIAS, groupId = GROUP_ID)
    public void listen(ArqEvent<?> eventArch) {
        procesarEvento(eventArch);
    }

    @Override
    public void procesarEvento(ArqEvent<?> eventArch) {
        if (!arqConfigProperties.isEventBrokerActive()) {
            logger.error("Debe tener activa la configuración de uso de mensajería en la arquitectura");
            return;
        }
        this.eventStoreInputPort.saveEvent(eventArch.getArqContextInfo().getApplicationId(),
                eventArch.getArqContextInfo().getAlmacen(), eventArch.getId(), eventArch);
    }


}
