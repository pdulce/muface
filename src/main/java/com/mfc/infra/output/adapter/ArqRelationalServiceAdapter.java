package com.mfc.infra.output.adapter;

import com.mfc.infra.configuration.ArqConfigProperties;
//import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.dto.ArqAbstractDTO;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.exceptions.ArqNotExistException;
import com.mfc.infra.output.port.ArqRelationalServicePort;
import com.mfc.infra.utils.ArqConversionUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
public abstract class ArqRelationalServiceAdapter<T, D extends IArqDTO, ID> implements ArqRelationalServicePort<T, D, ID> {
    Logger logger = LoggerFactory.getLogger(ArqRelationalServiceAdapter.class);
    @Autowired
    ArqConfigProperties arqConfigProperties;

    //@Autowired(required = false)
    //ArqCommandEventPublisherPort arqCommandEventPublisherPort;

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

    protected Class<D> getClassOfDTO() {
        Class<D> entityClass = (Class<D>) ((ParameterizedType) getClass()
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
    public D crear(D entityDto) {
        T entity = ArqAbstractDTO.convertToEntity(entityDto, getClassOfEntity());
        this.getRepository().save(entity);
        if (entity != null && arqConfigProperties.isEventBrokerActive()) {
            /*** Mando el evento al bus para que los recojan los dos consumers:
             *  - consumer responsable del dominio de eventos que persiste en MongoDB (patrón Event-Sourcing)
             *  - consumer responsable del dominio de consultas que persiste en Redis (patrón CQRS)
             */
            /*ArqEvent eventArch = new ArqEvent(getDocumentEntityClassname(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(saved).get("id").toString(),
                    ArqEvent.EVENT_TYPE_CREATE, saved);
            arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);*/
        }
        entityDto = ArqAbstractDTO.convertToDTO(entity, getClassOfDTO());
        return entityDto;
    }


    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public D actualizar(D entityDto) {
        T entity = ArqAbstractDTO.convertToEntity(entityDto, getClassOfEntity());
        ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
        if (!this.getRepository().findById(id).isPresent()) {
            ArqNotExistException e = new ArqNotExistException();
            e.setMsgError("entity with id: " + String.valueOf(id) + " not found");
            RuntimeException exc = new RuntimeException(e);
            throw exc;
        }
        this.getRepository().save(entity);
        if (entity != null && arqConfigProperties.isEventBrokerActive()) {
            /*ArqEvent eventArch = new ArqEvent(getDocumentEntityClassname(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(entity).get("id").toString(),
                    ArqEvent.EVENT_TYPE_UPDATE, updated);
            arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);*/
        }
        return ArqAbstractDTO.convertToDTO(entity, getClassOfDTO());
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public int borrar(D entityDto) {
        T entity = ArqAbstractDTO.convertToEntity(entityDto, getClassOfEntity());
        ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
        if (!this.getRepository().findById(id).isPresent()) {
            ArqNotExistException e = new ArqNotExistException();
            e.setMsgError("entity with id: " + String.valueOf(id) + " not found");
            RuntimeException exc = new RuntimeException(e);
            throw exc;
        }
        this.getRepository().delete(entity);
        if (arqConfigProperties.isEventBrokerActive()) {
            /*ArqEvent eventArch = new ArqEvent(getDocumentEntityClassname(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(entity).get("id").toString(),
                    ArqEvent.EVENT_TYPE_DELETE, entity);
            arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);*/
        }
        return !this.getRepository().findById(id).isPresent() ? 1 : 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public int borrar(List<D> entities) {
        AtomicInteger counter = new AtomicInteger();
        entities.forEach((record) -> {
            this.borrar(record);
            counter.getAndIncrement();
            if (arqConfigProperties.isEventBrokerActive()) {
                /*ArqEvent eventArch = new ArqEvent(getDocumentEntityClassname(), "author",
                        arqConfigProperties.getApplicationId(),
                        ArqConversionUtils.convertToMap(record).get("id").toString(),
                        ArqEvent.EVENT_TYPE_DELETE, record);
                arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);*/
            }
        });
        return counter.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void borrar() {
        //List<ArqEvent> events = new ArrayList<>();
        buscarTodos().forEach((record) -> {
            /*events.add(new ArqEvent(getDocumentEntityClassname(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(record).get("id").toString(),
                    ArqEvent.EVENT_TYPE_DELETE, record));*/
        });
        this.getRepository().deleteAll();
        /*if (arqConfigProperties.isEventBrokerActive()) {
            events.forEach((event) -> {
                arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, event);
            });
        }*/
    }

    @SuppressWarnings("unchecked")
    @Override
    public D buscarPorId(ID id) {
        if (!this.getRepository().findById(id).isPresent()) {
            logger.info("buscarPorId no localizó el id: " + id);
            ArqNotExistException e = new ArqNotExistException();
            e.setMsgError("id: " + id);
            RuntimeException exc = new RuntimeException(e);
            throw exc;
        }
        return ArqAbstractDTO.convertToDTO(this.getRepository().findById(id).get(), getClassOfDTO());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<D> buscarTodos() {
        List<D> resultado = new ArrayList<>();
        this.getRepository().findAll().stream().toList().forEach((entity) -> {
            resultado.add(ArqAbstractDTO.convertToDTO(entity, getClassOfDTO()));
        });
        return resultado;
    }

    /** método generíco para buscar dentro de cualquier campo de un entidad T **/

    @SuppressWarnings("unchecked")
    @Override
    public List<D> buscarCoincidenciasEstricto(D instanceDTO) {

        try {
            Class<T> entityClass = getClassOfEntity();
            T instance = ArqAbstractDTO.convertToEntity(instanceDTO, entityClass);
            List<D> resultado = new ArrayList<>();
            this.getRepository().findAll(Example.of(instance)).forEach((entity) -> {
                resultado.add(ArqAbstractDTO.convertToDTO(entity, getClassOfDTO()));
            });
            return resultado;
        } catch (Throwable exc1) {
            logger.error("Error in buscarPorCampoValor method: ", exc1.getCause());
            RuntimeException exc = new RuntimeException(exc1.getCause());
            throw exc;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<D> buscarCoincidenciasNoEstricto(D filterObject) {
        try {
            Class<T> entityClass = getClassOfEntity();
            T instance = ArqAbstractDTO.convertToEntity(filterObject, entityClass);
            List<D> resultado = new ArrayList<>();

            // Crear un ExampleMatcher con configuración de LIKE en todos los campos
            ExampleMatcher matcher = ExampleMatcher.matchingAll()
                    .withStringMatcher(StringMatcher.CONTAINING) // Realiza búsquedas LIKE %valor%
                    .withIgnoreCase(); // Ignorar mayúsculas/minúsculas

            // Crear el Example con el matcher configurado
            Example<T> example = Example.of(instance, matcher);

            // Buscar usando el repositorio
            this.getRepository().findAll(example).forEach((entity) -> {
                resultado.add(ArqAbstractDTO.convertToDTO(entity, getClassOfDTO()));
            });

            return resultado;
        } catch (Throwable exc1) {
            logger.error("Error in buscarCoincidenciasNoEstricto method: ", exc1);
            RuntimeException exc = new RuntimeException(exc1);
            throw exc;
        }
    }

}
