package com.mfc.infra.output.adapter;

import com.mfc.infra.configuration.ArqConfigProperties;
import com.mfc.infra.dto.ArqAbstractDTO;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.utils.ArqConversionUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Transactional
public abstract class ArqAbstractService<T, D extends IArqDTO, ID> {

    @Autowired
    ArqConfigProperties arqConfigProperties;

    @Autowired
    MessageSource messageSource;

    //@Autowired(required = false)
    //ArqCommandEventPublisherPort arqCommandEventPublisherPort;
    protected void registrarEvento(T entity, String eventType) {
        if (entity != null && arqConfigProperties.isEventBrokerActive()) {
            ArqEvent eventArch = new ArqEvent(entity.getClass().getSimpleName(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(entity).get("id").toString(),
                    eventType, entity);
            //arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);
        }
    }

    protected void registrarEventos(List<T> entities, String eventType) {
        entities.forEach((entity) -> {
            registrarEvento(entity, eventType);
        });
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

    protected List<D> convertToDTOList(List<T> entities) {
        List<D> dtos = new ArrayList<>();
        for (T entity : entities) {
            dtos.add(ArqAbstractDTO.convertToDTO(entity, getClassOfDTO()));
        }
        return dtos;
    }

}
