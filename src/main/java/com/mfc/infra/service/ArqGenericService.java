package com.mfc.infra.service;

import com.mfc.infra.configuration.ArqConfigProperties;
import com.mfc.infra.dto.ArqAbstractDTO;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.exceptions.ArqBaseOperationsException;
import com.mfc.infra.exceptions.NotExistException;
import com.mfc.infra.repository.ArqPortRepository;
import com.mfc.infra.utils.ArqConstantMessages;
import com.mfc.infra.utils.ArqConversionUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Profile;

import java.util.*;

@Transactional
@Service
public class ArqGenericService<T, D extends IArqDTO, ID> implements ArqServicePort<T, D, ID> {

    Logger logger = LoggerFactory.getLogger(ArqGenericService.class);

    @Autowired
    ArqConfigProperties arqConfigProperties;

    //@Autowired(required = false)
    //ArqCommandEventPublisherPort arqCommandEventPublisherPort;

    @Autowired
    MessageSource messageSource;

    private ArqPortRepository<T, ID> commonRepository;

    private Class<D> dtoClass;

    @Autowired
    public ArqGenericService(Map<Class<?>, ArqPortRepository<?, ID>> commonRepositories) {
        if (!commonRepositories.isEmpty()) {
            ArqPortRepository<?, ID> commonRepo = commonRepositories.values().iterator().next();
            this.commonRepository = (ArqPortRepository<T, ID>) commonRepo;
        } else {
            throw new RuntimeException("No hay repositorios definidos");
        }
    }

    public void setDtoClass(Class<D> dtoClass) {
        this.dtoClass = dtoClass;
    }


    public void registrarEvento(T entity, String eventType) {
        if (entity != null && arqConfigProperties.isEventBrokerActive()) {
            ArqEvent eventArch = new ArqEvent(entity.getClass().getSimpleName(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(entity).get("id").toString(),
                    eventType, entity);
            //arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);
        }
    }
    public void registrarEventos(List<T> entities, String eventType) {
        entities.forEach((entity) -> {
            registrarEvento(entity, eventType);
        });
    }


    @Override
    @Transactional
    public D crear(D entityDto) {
        try{
            T entity = ArqAbstractDTO.convertToEntity(entityDto, this.commonRepository.getClassOfEntity());
            this.commonRepository.save(entity);
            Class<D> dtoClass = (Class<D>) entityDto.getClass();
            entityDto = ArqAbstractDTO.convertToDTO(entity, dtoClass);
            this.registrarEvento(entity, ArqEvent.EVENT_TYPE_CREATE);
            String info = messageSource.getMessage(ArqConstantMessages.CREATED_OK,
                    new Object[]{this.getCollectionName()}, new Locale("es"));
            logger.info(info);
            return entityDto;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.CREATED_KO,
                    new Object[]{this.getCollectionName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.CREATED_KO,
                    new Object[]{this.getCollectionName(), exc.getCause()});
        }
    }

    @Override
    @Transactional
    public D actualizar(D entityDto) {
        try {
            T entity = ArqAbstractDTO.convertToEntity(entityDto, this.commonRepository.getClassOfEntity());
            ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
            if (this.buscarPorId(id) == null) {
                throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{this.getCollectionName(), id});
            }
            this.commonRepository.save(entity);
            this.registrarEvento(entity, ArqEvent.EVENT_TYPE_UPDATE);
            String info = messageSource.getMessage(ArqConstantMessages.UPDATED_OK,
                    new Object[]{this.getCollectionName()}, new Locale("es"));
            logger.info(info);
            Class<D> dtoClass = (Class<D>) entityDto.getClass();
            return ArqAbstractDTO.convertToDTO(entity, dtoClass);
        } catch (ArqBaseOperationsException opeExc) {
            throw opeExc;
        } catch (NotExistException notExists) {
            throw notExists;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.UPDATED_KO,
                    new Object[]{this.getCollectionName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.UPDATED_KO,
                    new Object[]{this.getCollectionName(), exc.getCause()});
        }
    }

    @Override
    @Transactional
    public int borrarEntidades(D entityDto) {
        try {
            T entity = ArqAbstractDTO.convertToEntity(entityDto, this.commonRepository.getClassOfEntity());
            ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
            if (this.buscarPorId(id) == null) {
                throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{this.getCollectionName(), id});
            }
            this.commonRepository.delete(entity);
            String info = messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                    new Object[]{this.getCollectionName()}, new Locale("es"));
            logger.info(info);
            this.registrarEvento(entity, ArqEvent.EVENT_TYPE_DELETE);
            return 1;
        } catch (ArqBaseOperationsException opeExc) {
            throw opeExc;
        } catch (NotExistException notExists) {
            throw notExists;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_KO,
                    new Object[]{this.getCollectionName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{this.getCollectionName(), exc.getCause()});
        }
    }

    @Override
    @Transactional
    public int borrarEntidades(List<D> entities) {
        try{
            entities.forEach((entityDTO) -> {
                borrarEntidades(entityDTO);
            });
            String info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                    new Object[]{this.getCollectionName()}, new Locale("es"));
            logger.info(info);
            return entities.size();
        } catch (ArqBaseOperationsException notExists) {
            throw notExists;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{this.getCollectionName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{this.getCollectionName(), exc.getCause()});
        }
    }

    @Override
    @Transactional
    public void borrarTodos() {
        try {
            List<T> entities = new ArrayList<>();
            buscarTodos().forEach((entityDTO) -> {
                T entity = ArqAbstractDTO.convertToEntity(entityDTO, this.commonRepository.getClassOfEntity());
                entities.add(entity);
            });
            if (entities.isEmpty()) {
                String info = messageSource.getMessage(ArqConstantMessages.NOTHING_TO_DELETE, null,
                        new Locale("es"));
                logger.info(info);
                return;
            }
            this.commonRepository.deleteEntities(entities);
            String info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                    new Object[]{this.getCollectionName()}, new Locale("es"));
            logger.info(info);
            this.registrarEventos(entities, ArqEvent.EVENT_TYPE_DELETE);
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{this.getCollectionName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{this.getCollectionName(), exc.getCause()});
        }
    }

    @Override
    public D buscarPorId(ID id) {
        Optional<T> result = this.commonRepository.findById(id);
        D item = result.isPresent() ? ArqAbstractDTO.convertToDTO(result.get(), getClassOfDTO()) : null;
        if (item == null) {
            throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                    new Object[]{this.getCollectionName(), id});
        }
        return item;
    }

    @Override
    public List<D> buscarTodos() {
        List<T> entities = this.commonRepository.findAll();
        return convertToDTOList(entities);
    }

    @Override
    public List<D> buscarCoincidenciasEstricto(D filterObject) {
        try {
            Class<T> entityClass = this.commonRepository.getClassOfEntity();
            T instance = ArqAbstractDTO.convertToEntity(filterObject, entityClass);

            List<D> resultado = new ArrayList<>();
            this.commonRepository.findByExampleStricted(instance).forEach((entity) -> {
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
            Class<T> entityClass = this.commonRepository.getClassOfEntity();
            T instance = ArqAbstractDTO.convertToEntity(filterObject, entityClass);
            List<D> resultado = new ArrayList<>();

            // Buscar usando el repositorio
            this.commonRepository.findByExampleNotStricted(instance).forEach((entity) -> {
                resultado.add(ArqAbstractDTO.convertToDTO(entity, getClassOfDTO()));
            });

            return resultado;
        } catch (Throwable exc1) {
            logger.error("Error in buscarCoincidenciasNoEstricto method: ", exc1);
            RuntimeException exc = new RuntimeException(exc1);
            throw exc;
        }
    }

    /** ******** PRIVATE METHODS ********** */


    private Class<D> getClassOfDTO() {
        return this.dtoClass;
    }

    private List<D> convertToDTOList(List<T> entities) {
        List<D> dtos = new ArrayList<>();
        for (T entity : entities) {
            dtos.add(ArqAbstractDTO.convertToDTO(entity, getClassOfDTO()));
        }
        return dtos;
    }

    private String getCollectionName() {
        Class<?> clazz = this.commonRepository.getClassOfEntity();
        if (clazz.isAnnotationPresent(Document.class)) {
            Document document = clazz.getAnnotation(Document.class);
            return document.collection();
        } else {
            return clazz.getSimpleName();
        }
    }

}
