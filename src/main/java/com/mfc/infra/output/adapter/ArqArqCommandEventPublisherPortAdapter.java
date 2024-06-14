package com.mfc.infra.output.adapter;

import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.output.port.ArqCommandEventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "arch.event-broker-active", havingValue = "true", matchIfMissing = false)
public class ArqArqCommandEventPublisherPortAdapter implements ArqCommandEventPublisherPort {

    Logger logger = LoggerFactory.getLogger(ArqArqCommandEventPublisherPortAdapter.class);
    private final KafkaTemplate<String, ArqEvent<?>> kafkaTemplate;

    @Autowired
    public ArqArqCommandEventPublisherPortAdapter(KafkaTemplate<String, ArqEvent<?>> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String topic, ArqEvent<?> eventArch) {
        kafkaTemplate.send(topic, eventArch.getId(), eventArch);
    }

}
