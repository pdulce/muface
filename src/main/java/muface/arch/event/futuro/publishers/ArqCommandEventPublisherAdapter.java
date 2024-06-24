package muface.arch.event.futuro.publishers;

import muface.arch.event.ArqEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "arch.event-broker-active", havingValue = "true", matchIfMissing = false)
public class ArqCommandEventPublisherAdapter implements ArqCommandEventPublisherPort {

    Logger logger = LoggerFactory.getLogger(ArqCommandEventPublisherAdapter.class);

    private final KafkaTemplate<String, ArqEvent<?>> kafkaTemplate;

    @Autowired
    public ArqCommandEventPublisherAdapter(KafkaTemplate<String, ArqEvent<?>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String topic, ArqEvent<?> eventArch) {
        kafkaTemplate.send(topic, eventArch.getId(), eventArch);
    }


}
