package com.mfc.infra.event.futuro.output;

import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.event.ArqSagaStepEventDocument;
import com.mfc.infra.event.futuro.repository.ArqSagaStepMongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(name = "arch.event-broker-active", havingValue = "true", matchIfMissing = false)
public class ArqSagaStepStoreAdapter implements ArqSagaStepInputPort {

    Logger logger = LoggerFactory.getLogger(ArqSagaStepStoreAdapter.class);
    private final MongoTemplate auditMongoTemplate;
    private final ArqSagaStepMongoRepository repository;

    @Autowired
    public ArqSagaStepStoreAdapter(
            @Qualifier("auditMongoTemplate") MongoTemplate auditMongoTemplate,
            ArqSagaStepMongoRepository auditRepository) {
        this.auditMongoTemplate = auditMongoTemplate;
        this.repository = auditRepository;
    }

    @Override
    public void saveSagaStep(String applicationId, String saga, String stepId, ArqEvent<?> eventArch) {
        try {
            ArqSagaStepEventDocument eventDocument = new ArqSagaStepEventDocument();
            eventDocument.setApplicationId(applicationId);
            eventDocument.setSaga(saga);
            eventDocument.setStepId(stepId);
            eventDocument.setEvent(eventArch);

            repository.save(eventDocument);
            logger.info("Evento de Auditoría almacenado de forma satisfactoria: {}", eventDocument);
        } catch (Exception e) {
            logger.error("Error almacenando evento de auditoría: {}", e.getMessage());
        }
    }

    @Override
    public void update(String applicationId, String saga, String stepId, String idEntry, ArqEvent<?> eventArch) {
        try {
            List<ArqSagaStepEventDocument> events = repository.findByApplicationIdAndSagaAndStepId(applicationId,
                    saga, stepId);
            for (ArqSagaStepEventDocument event : events) {
                if (event.getIdEntry().equals(idEntry)) {
                    event.setEvent(eventArch);
                    repository.save(event);
                    logger.info("Event updated successfully: {}", event);
                    return;
                }
            }
            logger.warn("Event not found for update: type={}, applicationId={}, almacen={}, entityId={}, idEntry={}",
                    applicationId, saga, stepId, idEntry);
        } catch (Exception e) {
            logger.error("Error updating event: {}", e.getMessage());
        }
    }

    @Override
    public List<Object> findAggregateByAppAndStoreAndAggregateId(String applicationId, String saga, String stepId) {
        try {
            List<ArqSagaStepEventDocument> events = repository.findByApplicationIdAndSagaAndStepId(applicationId,
                    saga, stepId);
            return events.stream().map(ArqSagaStepEventDocument::getEvent).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error finding aggregate: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Object> findAllByAppAndSaga(String applicationId, String saga) {
        try {
            List<ArqSagaStepEventDocument> events = repository.findByApplicationIdAndSaga(applicationId, saga);
            return events.stream().map(ArqSagaStepEventDocument::getEvent).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error finding events: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Object> findAllByApp(String applicationId) {
        try {
            List<ArqSagaStepEventDocument> events = repository.findByApplicationId(applicationId);
            return events.stream().map(ArqSagaStepEventDocument::getEvent).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error finding events: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Object> findAll() {
        try {
            List<ArqSagaStepEventDocument> events = repository.findAll();
            return events.stream().map(ArqSagaStepEventDocument::getEvent).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error finding events: {}", e.getMessage());
            return null;
        }
    }


}
