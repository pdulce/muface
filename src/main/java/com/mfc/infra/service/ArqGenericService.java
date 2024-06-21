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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.xml.transform.Transformer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
//@Service
public abstract class ArqGenericService<D extends IArqDTO, ID> implements ArqServicePort<D, ID> {

    Logger logger = LoggerFactory.getLogger(ArqGenericService.class);

    @Autowired
    ArqConfigProperties arqConfigProperties;

    //@Autowired(required = false)
    //ArqCommandEventPublisherPort arqCommandEventPublisherPort;

    private Class<D> myDtoClass;

    @Autowired
    MessageSource messageSource;

    protected abstract ArqPortRepository<?, ID> getRepository();

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
                ArqPortRepository<Object, ID> commandRepo = (ArqPortRepository<Object, ID>) getRepository();
                entityDtoResultado.setEntity(commandRepo.save(entityDto.getEntity()));
                this.registrarEvento(entityDtoResultado.getEntity(), ArqEvent.EVENT_TYPE_CREATE);
                String info = messageSource.getMessage(ArqConstantMessages.CREATED_OK,
                        new Object[]{this.getCollectionName(this.getRepository())}, new Locale("es"));
                logger.info(info);
            } catch (ConstraintViolationException ctExc) {
                throw ctExc;
            } catch (Throwable exc) {
                String error = messageSource.getMessage(ArqConstantMessages.CREATED_KO,
                        new Object[]{this.getCollectionName(this.getRepository()), exc.getCause()},
                        new Locale("es"));
                logger.error(error);
                throw new ArqBaseOperationsException(ArqConstantMessages.CREATED_KO,
                        new Object[]{this.getCollectionName(this.getRepository()), exc.getCause()});
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
                D searched = buscarPorId((ID) entityDto.getId());
                ArqPortRepository<Object, ID> commandRepo = (ArqPortRepository<Object, ID>) getRepository();
                Object objToUpdate = entityDto.getEntity();
                entityDtoResultado.setEntity(commandRepo.save(objToUpdate));
                this.registrarEvento(entityDtoResultado.getEntity(), ArqEvent.EVENT_TYPE_UPDATE);
                String info = messageSource.getMessage(ArqConstantMessages.UPDATED_OK,
                        new Object[]{this.getCollectionName(this.getRepository())}, new Locale("es"));
                logger.info(info);
            } catch (ConstraintViolationException ctExc) {
                throw ctExc;
            } catch (NotExistException notExistException) {
                throw notExistException;
            } catch (Throwable exc) {
                String error = messageSource.getMessage(ArqConstantMessages.UPDATED_KO,
                        new Object[]{this.getCollectionName(this.getRepository()), exc.getCause()},
                        new Locale("es"));
                logger.error(error);
                throw new ArqBaseOperationsException(ArqConstantMessages.UPDATED_KO,
                        new Object[]{this.getCollectionName(this.getRepository()), exc.getCause()});
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
            ArqPortRepository<Object, ID> commandRepo = (ArqPortRepository<Object, ID>) getRepository();
            commandRepo.delete(entity);
            this.registrarEvento(entity, ArqEvent.EVENT_TYPE_DELETE);
            info = messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                    new Object[]{this.getCollectionName(this.getRepository())}, LocaleContextHolder.getLocale());
            logger.info(messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                    new Object[]{this.getCollectionName(this.getRepository())}, new Locale("es")));
        } catch (ConstraintViolationException ctExc) {
            throw ctExc;
        } catch (NotExistException notExistException) {
            throw notExistException;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_KO,
                    new Object[]{this.getCollectionName(this.getRepository()), exc.getCause()},
                    LocaleContextHolder.getLocale());
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{this.getCollectionName(this.getRepository()), exc.getCause()});
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
                    new Object[]{entities.size(), this.getCollectionName(this.getRepository())},
                    LocaleContextHolder.getLocale());
            logger.info(messageSource.getMessage(ArqConstantMessages.DELETED_LIST_OK,
                    new Object[]{this.getCollectionName(this.getRepository())}, new Locale("es")));
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
            if (this.getRepository().findAll().isEmpty()) {
                info = messageSource.getMessage(ArqConstantMessages.NOTHING_TO_DELETE, null,
                        LocaleContextHolder.getLocale());
                logger.info(info);
            } else {
                D entityDto = getClassOfDTO().getDeclaredConstructor().newInstance();
                try {
                    this.getRepository().deleteAll();
                    this.registrarEvento(entityDto, ArqEvent.EVENT_TYPE_DELETE);
                    info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                            new Object[]{this.getCollectionName(this.getRepository())}, LocaleContextHolder.getLocale());
                    logger.info(messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                            new Object[]{this.getCollectionName(this.getRepository())}, new Locale("es")));
                } catch (Throwable exc) {
                    String error = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_KO,
                            new Object[]{this.getCollectionName(this.getRepository()), exc.getCause()},
                            new Locale("es"));
                    logger.error(error);
                    throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                            new Object[]{this.getCollectionName(this.getRepository()), exc.getCause()});
                }
            }
            return info;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    @Override
    public List<D> buscarTodos() {
        List<D> resultado = new ArrayList<>();
        this.getRepository().findAll().forEach((entity) -> {
            try {
                D instanciaDTOResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                resultado.add(instanciaDTOResultado);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                     | InvocationTargetException noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                        new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return resultado;
    }
    @Override
    public List<D> buscarTodosConOrdenacion(Sort sort) {
        if (sort == null) {
            logger.error("Parámetro sort es nulo");
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{"Parámetro sort es nulo"});
        }
        List<D> resultado = new ArrayList<>();
        this.getRepository().findAll(sort).forEach((entity) -> {
            try {
                D instanciaDTOResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                resultado.add(instanciaDTOResultado);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                     | InvocationTargetException noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                        new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return resultado;
    }

    @Override
    public D buscarPorId(ID id) {
        try {
            D entityDtoResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
            Optional<?> optionalT = this.getRepository().findById(id);
            if (optionalT.isPresent()) {
                entityDtoResultado.setEntity(optionalT.orElse(null));
                return entityDtoResultado;
            } else {
                throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{this.getCollectionName(this.getRepository()), id});
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    @Override
    public List<D> buscarCoincidenciasEstricto(D filterObject) {
        List<D> resultado = new ArrayList<>();
        ArqPortRepository<Object, ID> commandRepo = (ArqPortRepository<Object, ID>) getRepository();
        commandRepo.findByExampleStricted(filterObject.getEntity()).forEach((entity) -> {
            try {
                D instanciaDTOResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                resultado.add(instanciaDTOResultado);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                     | InvocationTargetException noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                        new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return resultado;
    }

    @Override
    public List<D> buscarCoincidenciasNoEstricto(D filterObject) {
        List<D> resultado = new ArrayList<>();
        ArqPortRepository<Object, ID> commandRepo = (ArqPortRepository<Object, ID>) getRepository();
        commandRepo.findByExampleNotStricted(filterObject.getEntity()).forEach((entity) -> {
            try {
                D instanciaDTOResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                resultado.add(instanciaDTOResultado);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                     | InvocationTargetException noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                        new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return resultado;
    }

    /*** Consultas paginadas ***/
    @Override
    public Page<D> buscarCoincidenciasEstrictoPaginados(D filterObject, Pageable pageable) {
        if (pageable == null) {
            logger.error("Parámetro pageable es nulo");
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{"Parámetro pageable es nulo"});
        }
        ArqPortRepository<Object, ID> commandRepo = (ArqPortRepository<Object, ID>) getRepository();
        Page<Object> resultado = commandRepo.findByExampleStrictedPaginated(filterObject.getEntity(), pageable);
        return convertirAPageOfDtos(resultado, pageable);
    }

    @Override
    public Page<D> buscarCoincidenciasNoEstrictoPaginados(D filterObject, Pageable pageable) {
        if (pageable == null) {
            logger.error("Parámetro pageable es nulo");
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{"Parámetro pageable es nulo"});
        }
        ArqPortRepository<Object, ID> commandRepo = (ArqPortRepository<Object, ID>) getRepository();
        Page<Object> resultado = commandRepo.findByExampleNotStrictedPaginated(filterObject.getEntity(), pageable);
        return convertirAPageOfDtos(resultado, pageable);
    }

    @Override
    public Page<D> buscarTodosPaginados(Pageable pageable) {
        if (pageable == null) {
            logger.error("Parámetro pageable es nulo");
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{"Parámetro pageable es nulo"});
        }
        ArqPortRepository<Object, ID> commandRepo = (ArqPortRepository<Object, ID>) getRepository();
        Page<Object> resultado = commandRepo.findAllPaginated(pageable);
        return convertirAPageOfDtos(resultado, pageable);
    }

    private final Page<D> convertirAPageOfDtos(Page<Object> pageimpl, Pageable pageable) {
        List<D> listConverted = new ArrayList<>();
        pageimpl.stream().toList().forEach((entity) -> {
            try {
                D instanciaDTOResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                listConverted.add(instanciaDTOResultado);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                     | InvocationTargetException noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                        new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return new PageImpl<>(listConverted,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                pageimpl.getTotalElements());
    }

    private Class<D> getClassOfDTO() {
        if (myDtoClass == null) {
            myDtoClass = (Class<D>) ((ParameterizedType) getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }
        return this.myDtoClass;
    }

}
