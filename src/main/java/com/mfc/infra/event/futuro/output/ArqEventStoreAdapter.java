package com.mfc.infra.event.futuro.output;

import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.event.ArqEventDocument;
import com.mfc.infra.event.futuro.repository.ArqEventMongoRepository;
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
public class ArqEventStoreAdapter implements ArqEventStoreInputPort {

    Logger logger = LoggerFactory.getLogger(ArqEventStoreAdapter.class);

    private final MongoTemplate auditMongoTemplate;
    private final ArqEventMongoRepository repository;

    @Autowired
    public ArqEventStoreAdapter(@Qualifier("auditMongoTemplate") MongoTemplate auditMongoTemplate,
            ArqEventMongoRepository auditRepository) {
        this.auditMongoTemplate = auditMongoTemplate;
        this.repository = auditRepository;
    }


    @Override
    public void saveEvent(String applicationId, String almacen, String entityId, ArqEvent<?> eventArch) {
        try {
            ArqEventDocument eventDocument = new ArqEventDocument();
            eventDocument.setApplicationId(applicationId);
            eventDocument.setAlmacen(almacen);
            eventDocument.setEntityId(entityId);
            eventDocument.setEvent(eventArch);

            repository.save(eventDocument);
            logger.info("Evento de Auditoría almacenado de forma satisfactoria: {}", eventDocument);
        } catch (Exception e) {
            logger.error("Error almacenando evento de auditoría: {}", e.getMessage());
        }
    }

    @Override
    public void update(String applicationId, String almacen, String entityId, String idEntry, ArqEvent<?> eventArch) {
        try {
            List<ArqEventDocument> events = repository.findByApplicationIdAndAlmacenAndEntityId(applicationId, almacen, entityId);
            for (ArqEventDocument event : events) {
                if (event.getIdEntry().equals(idEntry)) {
                    event.setEvent(eventArch);
                    repository.save(event);
                    logger.info("Event updated successfully: {}", event);
                    return;
                }
            }
            logger.warn("Event not found for update: type={}, applicationId={}, almacen={}, entityId={}, idEntry={}", applicationId, almacen, entityId, idEntry);
        } catch (Exception e) {
            logger.error("Error updating event: {}", e.getMessage());
        }
    }

    @Override
    public List<Object> findAggregateByAppAndStoreAndAggregateId(String applicationId, String almacen, String entityId) {
        try {
            List<ArqEventDocument> events = repository.findByApplicationIdAndAlmacenAndEntityId(applicationId, almacen, entityId);
            return events.stream().map(ArqEventDocument::getEvent).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error finding aggregate: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Object> findAllByAppAndStore(String applicationId, String almacen) {
        try {
            List<ArqEventDocument> events = repository.findByApplicationIdAndAlmacen(applicationId, almacen);
            return events.stream().map(ArqEventDocument::getEvent).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error finding events: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Object> findAllByApp(String applicationId) {
        try {
            List<ArqEventDocument> events = repository.findByApplicationId(applicationId);
            return events.stream().map(ArqEventDocument::getEvent).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error finding events: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<Object> findAll() {
        try {
            List<ArqEventDocument> events = repository.findAll();
            return events.stream().map(ArqEventDocument::getEvent).collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error finding events: {}", e.getMessage());
            return null;
        }
    }


}
