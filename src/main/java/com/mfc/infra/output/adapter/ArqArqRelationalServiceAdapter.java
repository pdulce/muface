package com.mfc.infra.output.adapter;

import com.mfc.infra.configuration.ArqConfigProperties;
import com.mfc.infra.domain.ArqDTOConverter;
import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.exceptions.ArqNotExistException;
import com.mfc.infra.output.port.ArqCommandEventPublisherPort;
import com.mfc.infra.output.port.ArqRelationalServicePort;
import com.mfc.infra.utils.ArqConversionUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Transactional
public abstract class ArqArqRelationalServiceAdapter<T, IDTO, ID> implements ArqRelationalServicePort<T, IDTO, ID> {
    Logger logger = LoggerFactory.getLogger(ArqArqRelationalServiceAdapter.class);
    @Autowired
    ArqConfigProperties arqConfigProperties;

    @Autowired(required = false)
    ArqCommandEventPublisherPort arqCommandEventPublisherPort;

    protected abstract JpaRepository<T, ID> getRepository();

    public String getDocumentEntityClassname() {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
        return entityClass.getSimpleName().toLowerCase();
    }

    protected Class<T> getClassOfEntity() {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
        return entityClass;
    }

    protected Class<IDTO> getClassOfDTO() {
        Class<IDTO> entityClass = (Class<IDTO>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[1];
        return entityClass;
    }

    protected Class<ID> getClassOfID() {
        Class<ID> entityClass = (Class<ID>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[2];
        return entityClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public IDTO crear(IDTO entityDto) {
        T saved = this.getRepository().save(ArqDTOConverter.convertToEntity(entityDto, getClassOfEntity()));
        if (saved != null && arqConfigProperties.isEventBrokerActive()) {
            /*** Mando el evento al bus para que los recojan los dos consumers:
             *  - consumer responsable del dominio de eventos que persiste en MongoDB (patrón Event-Sourcing)
             *  - consumer responsable del dominio de consultas que persiste en Redis (patrón CQRS)
             */
            ArqEvent eventArch = new ArqEvent(getDocumentEntityClassname(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(saved).get("id").toString(),
                    ArqEvent.EVENT_TYPE_CREATE, saved);
            arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);
        }
        return ArqDTOConverter.convertToDTO(saved, getClassOfDTO());
    }


    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public IDTO actualizar(IDTO entityDto) {
        T entity = ArqDTOConverter.convertToEntity(entityDto, getClassOfEntity());
        ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
        if (!this.getRepository().findById(id).isPresent()) {
            ArqNotExistException e = new ArqNotExistException();
            e.setMsgError("entity with id: " + String.valueOf(id) + " not found");
            RuntimeException exc = new RuntimeException(e);
            throw exc;
        }
        T updated =  this.getRepository().save(entity);
        if (updated != null && arqConfigProperties.isEventBrokerActive()) {
            ArqEvent eventArch = new ArqEvent(getDocumentEntityClassname(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(entity).get("id").toString(),
                    ArqEvent.EVENT_TYPE_UPDATE, updated);
            arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);
        }
        return ArqDTOConverter.convertToDTO(updated, getClassOfDTO());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void borrar(IDTO entityDto) {
        T entity = ArqDTOConverter.convertToEntity(entityDto, getClassOfEntity());
        ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
        if (!this.getRepository().findById(id).isPresent()) {
            ArqNotExistException e = new ArqNotExistException();
            e.setMsgError("entity with id: " + String.valueOf(id) + " not found");
            RuntimeException exc = new RuntimeException(e);
            throw exc;
        }
        this.getRepository().delete(entity);
        if (arqConfigProperties.isEventBrokerActive()) {
            ArqEvent eventArch = new ArqEvent(getDocumentEntityClassname(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(entity).get("id").toString(),
                    ArqEvent.EVENT_TYPE_DELETE, entity);
            arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void borrar(List<IDTO> entities) {
        entities.forEach((record) -> {
            this.borrar(record);
            if (arqConfigProperties.isEventBrokerActive()) {
                ArqEvent eventArch = new ArqEvent(getDocumentEntityClassname(), "author",
                        arqConfigProperties.getApplicationId(),
                        ArqConversionUtils.convertToMap(record).get("id").toString(),
                        ArqEvent.EVENT_TYPE_DELETE, record);
                arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void borrar() {
        List<ArqEvent> events = new ArrayList<>();
        buscarTodos().forEach((record) -> {
            events.add(new ArqEvent(getDocumentEntityClassname(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(record).get("id").toString(),
                    ArqEvent.EVENT_TYPE_DELETE, record));
        });
        this.getRepository().deleteAll();
        if (arqConfigProperties.isEventBrokerActive()) {
            events.forEach((event) -> {
                arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, event);
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public IDTO buscarPorId(ID id) {
        if (!this.getRepository().findById(id).isPresent()) {
            logger.info("buscarPorId no localizó el id: " + id);
            ArqNotExistException e = new ArqNotExistException();
            e.setMsgError("id: " + id);
            RuntimeException exc = new RuntimeException(e);
            throw exc;
        }
        return ArqDTOConverter.convertToDTO(this.getRepository().findById(id).get(), getClassOfDTO());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<IDTO> buscarTodos() {
        List<IDTO> resultado = new ArrayList<>();
        this.getRepository().findAll().stream().toList().forEach((entity) -> {
            resultado.add(ArqDTOConverter.convertToDTO(entity, getClassOfDTO()));
        });
        return resultado;
    }

    /** método generíco para buscar dentro de cualquier campo de un entidad T **/

    @SuppressWarnings("unchecked")
    public List<IDTO> buscarPorCampoValor(String fieldName, Object fieldValue) {

        try {
            Class<T> entityClass = getClassOfEntity();
            T instance = entityClass.getDeclaredConstructor().newInstance();
            // llenamos esta instancia con el field y value recibidos
            Field field = entityClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, fieldValue);

            List<IDTO> resultado = new ArrayList<>();
            this.getRepository().findAll(Example.of(instance)).forEach((entity) -> {
                resultado.add(ArqDTOConverter.convertToDTO(entity, getClassOfDTO()));
            });
            return resultado;
        } catch (Throwable exc1) {
            logger.error("Error in buscarPorCampoValor method: ", exc1.getCause());
            ArqNotExistException e = new ArqNotExistException();
            e.setMsgError("fieldName: " + fieldName + " not exists for this Entity class");
            RuntimeException exc = new RuntimeException(e);
            throw exc;
        }
    }

}
