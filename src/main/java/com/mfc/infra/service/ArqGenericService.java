package com.mfc.infra.service;

import com.mfc.infra.configuration.ArqConfigProperties;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.exceptions.ArqBaseOperationsException;
import com.mfc.infra.exceptions.NotExistException;
import com.mfc.infra.repository.ArqPortRepository;
import com.mfc.infra.utils.ArqConstantMessages;
import com.mfc.infra.utils.ArqConversionUtils;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Transactional
@Service
public class ArqGenericService<D extends IArqDTO, ID> implements ArqServicePort<D, ID> {

    Logger logger = LoggerFactory.getLogger(ArqGenericService.class);

    @Autowired
    ArqConfigProperties arqConfigProperties;

    //@Autowired(required = false)
    //ArqCommandEventPublisherPort arqCommandEventPublisherPort;

    @Autowired
    MessageSource messageSource;

    private final Map<String, ArqPortRepository<?, ID>> commonRepositories = new HashMap<>();

    private Class<D> dtoClass;

    private ArqPortRepository<Object, ID> repositoryOfThisDto;

    @Autowired
    public ArqGenericService(Map<Class<?>, ArqPortRepository<?, ID>> commonRepositories) {
        if (!commonRepositories.isEmpty()) {
            Iterator<ArqPortRepository<?, ID>> iteratorRepositories = commonRepositories.values().iterator();
            while (iteratorRepositories.hasNext()) {
                ArqPortRepository<?, ID> repository = iteratorRepositories.next();
                this.commonRepositories.put(repository.getClassOfEntity().getName(), repository);
            }
        } else {
            throw new RuntimeException("No hay repositorios definidos");
        }
    }

    public void setDtoClass(Class<D> dtoClass) {
        this.dtoClass = dtoClass;
        String nameOfRepository = "";
        try {
            if (List.class.isAssignableFrom(dtoClass)) {
                // La clase es una subclase de List: extraemos los objetos de la misma
                Type genericSuperclass = dtoClass.getGenericSuperclass();
                if (genericSuperclass instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class) {
                        Class<?> listElementClass = (Class<?>) actualTypeArguments[0];
                        nameOfRepository = listElementClass.getName();
                    } else {
                        throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                           new Object[]{"No se pudo determinar la clase de los elementos de la lista."});
                    }
                } else {
                    throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                           new Object[]{"La clase no tiene un tipo genérico."});
                }
            } else {
                // La clase no es una subclase de List
                nameOfRepository = dtoClass.getDeclaredConstructor().newInstance().getEntity().getClass().getName();
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{dtoClass.getClass().getSimpleName(), noSuchMethodException.getCause()});
        }
        this.repositoryOfThisDto = (ArqPortRepository<Object, ID>) this.commonRepositories.get(nameOfRepository);
    }

    private String getCollectionName(ArqPortRepository<?, ID> commonRepository) {
        Class<?> clazz = commonRepository.getClassOfEntity();
        if (clazz.isAnnotationPresent(Document.class)) {
            Document document = clazz.getAnnotation(Document.class);
            return document.collection();
        } else {
            return clazz.getSimpleName();
        }
    }


    public void registrarEvento(Object entity, String eventType) {
        if (entity != null && arqConfigProperties.isEventBrokerActive()) {
            ArqEvent eventArch = new ArqEvent(entity.getClass().getSimpleName(), "author",
                    arqConfigProperties.getApplicationId(),
                    ArqConversionUtils.convertToMap(entity).get("id").toString(),
                    eventType, entity);
            //arqCommandEventPublisherPort.publish(ArqEvent.EVENT_TOPIC, eventArch);
        }
    }
    public void registrarEventos(List<Object> entities, String eventType) {
        entities.forEach((entity) -> {
            registrarEvento(entity, eventType);
        });
    }


    @Override
    @Transactional
    public D crear(D entityDto) {
        try {
            Class<D> dtoClass = (Class<D>) entityDto.getClass();
            D entityDtoResultado = dtoClass.getDeclaredConstructor().newInstance();
            try {
                entityDtoResultado.setEntity(this.repositoryOfThisDto.save(entityDto.getEntity()));
                this.registrarEvento(entityDtoResultado.getEntity(), ArqEvent.EVENT_TYPE_CREATE);
                String info = messageSource.getMessage(ArqConstantMessages.CREATED_OK,
                        new Object[]{this.getCollectionName(this.repositoryOfThisDto)}, new Locale("es"));
                logger.info(info);
            } catch (ConstraintViolationException ctExc) {
                throw ctExc;
            } catch (Throwable exc) {
                String error = messageSource.getMessage(ArqConstantMessages.CREATED_KO,
                        new Object[]{this.getCollectionName(this.repositoryOfThisDto), exc.getCause()},
                        new Locale("es"));
                logger.error(error);
                throw new ArqBaseOperationsException(ArqConstantMessages.CREATED_KO,
                        new Object[]{this.getCollectionName(this.repositoryOfThisDto), exc.getCause()});
            }
            return entityDtoResultado;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.UPDATED_KO,
                    new Object[]{entityDto.getClass().getSimpleName(), noSuchMethodException.getCause()});
        }
    }
    @Override
    @Transactional
    public D actualizar(D entityDto) {
        try {
            Class<D> dtoClass = (Class<D>) entityDto.getClass();
            D entityDtoResultado = dtoClass.getDeclaredConstructor().newInstance();
            try {
                buscarPorId((ID) entityDto.getId());
                entityDtoResultado.setEntity(this.repositoryOfThisDto.save(entityDto.getEntity()));
                this.registrarEvento(entityDtoResultado.getEntity(), ArqEvent.EVENT_TYPE_UPDATE);
                String info = messageSource.getMessage(ArqConstantMessages.UPDATED_OK,
                        new Object[]{this.getCollectionName(this.repositoryOfThisDto)}, new Locale("es"));
                logger.info(info);
            } catch (ConstraintViolationException ctExc) {
                throw ctExc;
            } catch (NotExistException notExistException) {
                throw notExistException;
            } catch (Throwable exc) {
                String error = messageSource.getMessage(ArqConstantMessages.UPDATED_KO,
                        new Object[]{this.getCollectionName(this.repositoryOfThisDto), exc.getCause()},
                        new Locale("es"));
                logger.error(error);
                throw new ArqBaseOperationsException(ArqConstantMessages.UPDATED_KO,
                        new Object[]{this.getCollectionName(this.repositoryOfThisDto), exc.getCause()});
            }
            return entityDtoResultado;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.UPDATED_KO,
                    new Object[]{entityDto.getClass().getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    @Override
    @Transactional
    public String borrarEntidad(ID id) {
        String info = "";
        try {
            Object entity = this.buscarPorId(id).getEntity();
            this.repositoryOfThisDto.delete(entity);
            this.registrarEvento(entity, ArqEvent.EVENT_TYPE_DELETE);
            info = messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                    new Object[]{this.getCollectionName(this.repositoryOfThisDto)}, new Locale("es"));
            logger.info(info);
        } catch (ConstraintViolationException ctExc) {
            throw ctExc;
        } catch (NotExistException notExistException) {
            throw notExistException;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_KO,
                    new Object[]{this.getCollectionName(this.repositoryOfThisDto), exc.getCause()},
                    new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{this.getCollectionName(this.repositoryOfThisDto), exc.getCause()});
        }
        return info;
    }


    @Override
    @Transactional
    public String borrarEntidad(D entityDto) {
        Object id = entityDto.getId();
        return this.borrarEntidad((ID) id);
    }

    @Override
    @Transactional
    public String borrarEntidades(List<D> entities) {
        String info = "";
        try{
            entities.forEach((entityDTO) -> {
                borrarEntidad(entityDTO);
            });
            info = messageSource.getMessage(ArqConstantMessages.DELETED_LIST_OK,
                    new Object[]{entities.size(), this.getCollectionName(this.repositoryOfThisDto)},
                    new Locale("es"));
            logger.info(info);
            return info;
        } catch (ConstraintViolationException ctExc) {
            throw ctExc;
        } catch (NotExistException notExistException) {
            throw notExistException;
        } catch (ArqBaseOperationsException arqBaseOperationsException) {
            throw arqBaseOperationsException;
        } catch (Throwable exc) {
            throw exc;
        }
    }

    @Override
    @Transactional
    public String borrarTodos() {
        String info = "";
        try {
            D entityDto = dtoClass.getDeclaredConstructor().newInstance();
            try {
                this.repositoryOfThisDto.deleteAll();
                this.registrarEvento(entityDto, ArqEvent.EVENT_TYPE_DELETE);
                info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                        new Object[]{this.getCollectionName(this.repositoryOfThisDto)}, new Locale("es"));
                logger.info(info);
            } catch (Throwable exc) {
                String error = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_KO,
                        new Object[]{this.getCollectionName(this.repositoryOfThisDto), exc.getCause()},
                        new Locale("es"));
                logger.error(error);
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                        new Object[]{this.getCollectionName(this.repositoryOfThisDto), exc.getCause()});
            }
            return info;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    @Override
    public List<D> buscarTodos() {
        List<D> resultado = new ArrayList<>();
        this.repositoryOfThisDto.findAll().forEach((entity) -> {
            D instanciaDTOResultado = null;
            try {
                instanciaDTOResultado = dtoClass.getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                resultado.add(instanciaDTOResultado);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                     | InvocationTargetException noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                        new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return resultado;
    }

    @Override
    public D buscarPorId(ID id) {
        try {
            D entityDtoResultado = dtoClass.getDeclaredConstructor().newInstance();
            Optional<?> optionalT = this.repositoryOfThisDto.findById(id);
            if (optionalT.isPresent()) {
                entityDtoResultado.setEntity(optionalT.get());
                return entityDtoResultado;
            } else {
                throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{this.getCollectionName(this.repositoryOfThisDto), id});
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    @Override
    public List<D> buscarCoincidenciasEstricto(D filterObject) {

        List<D> resultado = new ArrayList<>();
        this.repositoryOfThisDto.findByExampleStricted(filterObject.getEntity()).forEach((entity) -> {
            D instanciaDTOResultado = null;
            try {
                instanciaDTOResultado = dtoClass.getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                resultado.add(instanciaDTOResultado);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                     | InvocationTargetException noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                        new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return resultado;
    }

    @Override
    public List<D> buscarCoincidenciasNoEstricto(D filterObject) {

        List<D> resultado = new ArrayList<>();
        this.repositoryOfThisDto.findByExampleNotStricted(filterObject.getEntity()).forEach((entity) -> {
            D instanciaDTOResultado = null;
            try {
                instanciaDTOResultado = dtoClass.getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                resultado.add(instanciaDTOResultado);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                     | InvocationTargetException noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                        new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return resultado;
    }


}
