package com.mfc.infra.output.adapter;

import com.mfc.infra.dto.ArqAbstractDTO;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.exceptions.ArqBaseOperationsException;
import com.mfc.infra.output.port.ArqServicePort;
import com.mfc.infra.utils.ArqConstantMessages;
import com.mfc.infra.utils.ArqConversionUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

public abstract class ArqServiceMongoAdapter<T, D extends IArqDTO, ID> extends ArqAbstractService<T, D, ID>
        implements ArqServicePort<T, D, ID> {
    Logger logger = LoggerFactory.getLogger(ArqServiceMongoAdapter.class);

    protected abstract MongoRepository<T, String> getRepository();

    @Autowired
    MongoOperations mongoOperations;

    @Override
    @Transactional
    public D crear(D entityDto) {
        try{
            T entity = ArqAbstractDTO.convertToEntity(entityDto, getClassOfEntity());
            this.getRepository().save(entity);
            entityDto = ArqAbstractDTO.convertToDTO(entity, getClassOfDTO());
            super.registrarEvento(entity, ArqEvent.EVENT_TYPE_CREATE);
            String info = messageSource.getMessage(ArqConstantMessages.CREATED_OK,
                    new Object[]{getClassOfEntity().getSimpleName()}, new Locale("es"));
            logger.info(info);
            return entityDto;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.CREATED_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.CREATED_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()});
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public D actualizar(D entityDto) {
        try {
            T entity = ArqAbstractDTO.convertToEntity(entityDto, getClassOfEntity());
            ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
            if (this.buscarPorId(id) == null) {
                throw new ArqBaseOperationsException(ArqConstantMessages.RECORD_NOT_FOUND, new Object[]{id});
            }
            this.getRepository().save(entity);
            super.registrarEvento(entity, ArqEvent.EVENT_TYPE_UPDATE);
            String info = messageSource.getMessage(ArqConstantMessages.UPDATED_OK,
                    new Object[]{getClassOfEntity().getSimpleName()}, new Locale("es"));
            logger.info(info);
            return ArqAbstractDTO.convertToDTO(entity, getClassOfDTO());
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.UPDATED_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.UPDATED_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()});
        }
    }

    @Override
    @Transactional
    public int borrar(D entityDto) {
        try {
            T entity = ArqAbstractDTO.convertToEntity(entityDto, getClassOfEntity());
            ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
            if (this.buscarPorId(id) == null) {
                throw new ArqBaseOperationsException(ArqConstantMessages.RECORD_NOT_FOUND, new Object[]{id});
            }
            Query deleteQuery = new Query();
            Criteria criteria = Criteria.where("_id").is(id);
            deleteQuery.addCriteria(criteria);
            mongoOperations.remove(deleteQuery, entity.getClass());
            String info = messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                    new Object[]{getClassOfEntity().getSimpleName()}, new Locale("es"));
            logger.info(info);
            super.registrarEvento(entity, ArqEvent.EVENT_TYPE_DELETE);
            return 1;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()});
        }
    }

    @Override
    @Transactional
    public int borrar(List<D> entities) {
        try{
            entities.forEach((entityDTO) -> {
                borrar(entityDTO);
            });
            String info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                    new Object[]{getClassOfEntity().getSimpleName()}, new Locale("es"));
            logger.info(info);
            return entities.size();
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()});
        }
    }

    @Override
    @Transactional
    public void borrar() {
        try {
            List<T> entities = new ArrayList<>();
            buscarTodos().forEach((entityDTO) -> {
                T entity = ArqAbstractDTO.convertToEntity(entityDTO, getClassOfEntity());
                entities.add(entity);
            });
            String collectionName = getCollectionName(getClassOfEntity());
            mongoOperations.dropCollection(collectionName);
            String info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                    new Object[]{getClassOfEntity().getSimpleName()}, new Locale("es"));
            logger.info(info);
            super.registrarEventos(entities, ArqEvent.EVENT_TYPE_DELETE);
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()});
        }
    }

    private String getCollectionName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Document.class)) {
            Document document = clazz.getAnnotation(Document.class);
            return document.collection();
        }
        throw new IllegalStateException("La clase " + clazz.getName() + " no está anotada con @Document");
    }

    @Override
    public D buscarPorId(ID id) {
        Optional<T> result = getRepository().findById((String) id);
        D item = result.isPresent() ? ArqAbstractDTO.convertToDTO(result.isPresent(), getClassOfDTO()) : null;
        if (item == null) {
            throw new ArqBaseOperationsException(ArqConstantMessages.RECORD_NOT_FOUND, new Object[]{id});
        }
        return item;
    }

    @Override
    public List<D> buscarTodos() {
        List<T> entities = getRepository().findAll();
        return convertToDTOList(entities);
    }

    @Override
    public List<D> buscarCoincidenciasEstricto(D filterObject) {
        try {
            Class<T> entityClass = getClassOfEntity();
            T instance = ArqAbstractDTO.convertToEntity(filterObject, entityClass);

            ExampleMatcher matcher = ExampleMatcher.matchingAll()
                    .withIgnoreNullValues();
            Example<T> example = Example.of(instance, matcher);

            List<D> resultado = new ArrayList<>();
            getRepository().findAll(example).forEach((entity) -> {
                resultado.add(ArqAbstractDTO.convertToDTO(entity, getClassOfDTO()));
            });

            return resultado;
        } catch (Throwable exc1) {
            logger.error("Error in buscarCoincidenciasEstricto method: ", exc1);
            RuntimeException exc = new RuntimeException(exc1);
            throw exc;
        }
    }

    @Override
    public List<D> buscarCoincidenciasNoEstricto(D filterObject) {
        try {
            Class<T> entityClass = getClassOfEntity();
            T instance = ArqAbstractDTO.convertToEntity(filterObject, entityClass);
            List<D> resultado = new ArrayList<>();

            // Crear un ExampleMatcher con configuración de LIKE en todos los campos
            ExampleMatcher matcher = ExampleMatcher.matchingAll()
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // Realiza búsquedas LIKE %valor%
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


    // Métodos de conversión entre DTO y Entity



}
