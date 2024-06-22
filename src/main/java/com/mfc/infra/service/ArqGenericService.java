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
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@Transactional
public abstract class ArqGenericService<D extends IArqDTO, ID> implements ArqServicePort<D, ID> {

    Logger logger = LoggerFactory.getLogger(ArqGenericService.class);

    @Autowired
    ArqConfigProperties arqConfigProperties;

    //@Autowired(required = false)
    //ArqCommandEventPublisherPort arqCommandEventPublisherPort;

    private Class<D> myDtoClass;

    @Autowired
    protected ApplicationContext applicationContext;

    public abstract String getRepositoryEntityOfDTO();

    @Autowired
    MessageSource messageSource;

    private Class<D> getClassOfDTO() {
        if (myDtoClass == null) {
            myDtoClass = (Class<D>) ((ParameterizedType) getClass()
                    .getGenericSuperclass())
                    .getActualTypeArguments()[0];
        }
        return this.myDtoClass;
    }
    protected ArqPortRepository<Object, ID> getRepository() {
        String entityClassName = "";
        try {
            D entityDtoResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
            entityClassName = entityDtoResultado.getEntity().getClass().getName();
        } catch (Throwable exc) {
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{"Error recuperando repositorio de la entidad " + entityClassName});
        }

        for (String repoName : commandRepositories.keySet()) {
            if (getRepositoryEntityOfDTO().contentEquals(entityClassName)) {
                return (ArqPortRepository<Object, ID>) commandRepositories.get(repoName);
            }
        }
        throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                new Object[]{"Error recuperando repositorio de la entidad " + entityClassName});
    }
    Map<String, ArqPortRepository<?, ID>> commandRepositories;

    @Autowired
    public ArqGenericService(Map<String, ArqPortRepository<?, ID>> repositories) {
        this.commandRepositories = repositories;
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
    public D insertar(D entityDto) {
        try {
            Class<D> dtoClass = (Class<D>) entityDto.getClass();
            D entityDtoResultado = dtoClass.getDeclaredConstructor().newInstance();
            try {
                ArqPortRepository<Object, ID> commandRepo = getRepository();
                entityDtoResultado.setEntity(commandRepo.save(entityDto.getEntity()));
                this.registrarEvento(entityDtoResultado.getEntity(), ArqEvent.EVENT_TYPE_CREATE);
                String info = messageSource.getMessage(ArqConstantMessages.CREATED_OK,
                        new Object[]{getClassOfDTO().getSimpleName()}, new Locale("es"));
                logger.info(info);
            } catch (ConstraintViolationException ctExc) {
                throw ctExc;
            } catch (Throwable exc) {
                String error = messageSource.getMessage(ArqConstantMessages.CREATED_KO,
                        new Object[]{getClassOfDTO().getSimpleName(), exc.getCause()},
                        new Locale("es"));
                logger.error(error);
                throw new ArqBaseOperationsException(ArqConstantMessages.CREATED_KO,
                        new Object[]{getClassOfDTO().getSimpleName(), exc.getCause()});
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
            ArqPortRepository<Object, ID> commandRepo = getRepository();
            Optional<?> optionalT = commandRepo.findById((ID) entityDto.getId());
            if (optionalT.isPresent()) {
                Object searched = optionalT.orElse(null);
                entityDto.actualizarEntidad(searched);
                Object updated = commandRepo.save(searched);
                this.registrarEvento(updated, ArqEvent.EVENT_TYPE_UPDATE);
                String info = messageSource.getMessage(ArqConstantMessages.UPDATED_OK,
                        new Object[]{getClassOfDTO().getSimpleName()}, new Locale("es"));
                logger.info(info);
            } else {
                throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{getClassOfDTO().getSimpleName(), (ID) entityDto.getId()});
            }
        } catch (ConstraintViolationException | NotExistException ctExc) {
            throw ctExc;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.UPDATED_KO,
                    new Object[]{getClassOfDTO().getSimpleName(), exc.getCause()},
                    new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.UPDATED_KO,
                    new Object[]{getClassOfDTO().getSimpleName(), exc.getCause()});
        }
        return entityDto;
    }

    @Override
    @Transactional
    public String borrarEntidad(ID id) {
        String info = "";
        try {
            ArqPortRepository<Object, ID> commandRepo = getRepository();
            Optional<?> optionalT = commandRepo.findById(id);
            if (optionalT.isPresent()) {
                Object entity = optionalT.orElse(null);
                commandRepo.delete(entity);
                this.registrarEvento(entity, ArqEvent.EVENT_TYPE_DELETE);
                info = messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                        new Object[]{getClassOfDTO().getSimpleName()}, LocaleContextHolder.getLocale());
                logger.info(messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                        new Object[]{getClassOfDTO().getSimpleName()}, new Locale("es")));
            } else {
                throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{getClassOfDTO().getSimpleName(), id});
            }
        } catch (ConstraintViolationException | NotExistException ctExc) {
            throw ctExc;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_KO,
                    new Object[]{getClassOfDTO().getSimpleName(), exc.getCause()},
                    LocaleContextHolder.getLocale());
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{getClassOfDTO().getSimpleName(), exc.getCause()});
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
                    new Object[]{entities.size(), getClassOfDTO().getSimpleName()},
                    LocaleContextHolder.getLocale());
            logger.info(messageSource.getMessage(ArqConstantMessages.DELETED_LIST_OK,
                    new Object[]{getClassOfDTO().getSimpleName()}, new Locale("es")));
            return info;
        } catch (Throwable exc) {
            throw exc;
        }
    }

    @Override
    @Transactional
    public String borrarTodos() {
        String info = "";
        try {
            ArqPortRepository<Object, ID> commandRepo = getRepository();
            if (commandRepo.findAll().isEmpty()) {
                info = messageSource.getMessage(ArqConstantMessages.NOTHING_TO_DELETE, null,
                        LocaleContextHolder.getLocale());
                logger.info(info);
            } else {
                D entityDto = getClassOfDTO().getDeclaredConstructor().newInstance();
                try {
                    commandRepo.deleteAll();
                    this.registrarEvento(entityDto, ArqEvent.EVENT_TYPE_DELETE);
                    info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                            new Object[]{getClassOfDTO().getSimpleName()}, LocaleContextHolder.getLocale());
                    logger.info(messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                            new Object[]{getClassOfDTO().getSimpleName()}, new Locale("es")));
                } catch (Throwable exc) {
                    String error = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_KO,
                            new Object[]{getClassOfDTO().getSimpleName(), exc.getCause()},
                            new Locale("es"));
                    logger.error(error);
                    throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                            new Object[]{getClassOfDTO().getSimpleName(), exc.getCause()});
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
        if (id == null) {
            throw new NotExistException(ArqConstantMessages.ERROR_BAD_REQUEST,
                    new Object[]{getClassOfDTO().getSimpleName(), "id: <null>"});
        }
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        try {
            D entityDtoResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
            Optional<?> optionalT = commandRepo.findById(id);
            if (optionalT.isPresent()) {
                entityDtoResultado.setEntity(optionalT.orElse(null));
                return entityDtoResultado;
            } else {
                throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{getClassOfDTO().getSimpleName(), id});
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    @Override
    public List<D> buscarPorIds(List<ID> ids) {
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        List<Object> resultado = commandRepo.findByIds(ids);
        return convertirListaADtos(resultado);
    }

    @Override
    public List<D> buscarCoincidenciasEstricto(D filterObject) {
        List<D> resultado = new ArrayList<>();
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        commandRepo.findByExampleStricted(filterObject.getEntity()).forEach((entity) -> {
            try {
                D instanciaDTOResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                resultado.add(instanciaDTOResultado);
            } catch (Throwable noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                        new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return resultado;
    }

    @Override
    public List<D> buscarCoincidenciasNoEstricto(D filterObject) {
        List<D> resultado = new ArrayList<>();
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        commandRepo.findByExampleNotStricted(filterObject.getEntity()).forEach((entity) -> {
            try {
                D instanciaDTOResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                resultado.add(instanciaDTOResultado);
            } catch (Throwable noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                        new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return resultado;
    }

    /*** Consultas paginadas ***/
    @Override
    public Page<D> buscarCoincidenciasEstrictoPaginados(D filterObject, Pageable pageable) {
        Pageable newPageable = ArqConversionUtils.changePageableOrderFields(filterObject, pageable);
        if (newPageable == null) {
            logger.error("Parámetro pageable es nulo");
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{"Parámetro pageable es nulo"});
        }
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        Page<Object> resultado = commandRepo.findByExampleStrictedPaginated(filterObject.getEntity(), newPageable);
        return convertirAPageOfDtos(resultado, newPageable);
    }

    @Override
    public Page<D> buscarCoincidenciasNoEstrictoPaginados(D filterObject, Pageable pageable) {
        Pageable newPageable = ArqConversionUtils.changePageableOrderFields(filterObject, pageable);
        if (newPageable == null) {
            logger.error("Parámetro pageable es nulo");
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{"Parámetro pageable es nulo"});
        }
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        Page<Object> resultado = commandRepo.findByExampleNotStrictedPaginated(filterObject.getEntity(), newPageable);
        return convertirAPageOfDtos(resultado, newPageable);
    }

    @Override
    public Page<D> buscarTodosPaginados(Pageable pageable) {
        try {
            D instanciaDTOResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
            Pageable newPageable = ArqConversionUtils.changePageableOrderFields(instanciaDTOResultado, pageable);
            if (newPageable == null) {
                logger.error("Parámetro pageable es nulo");
                throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                        new Object[]{"Parámetro pageable es nulo"});
            }
            ArqPortRepository<Object, ID> commandRepo = getRepository();
            Page<Object> resultado = commandRepo.findAllPaginated(newPageable);
            return convertirAPageOfDtos(resultado, newPageable);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    protected final Page<D> convertirAPageOfDtos(Page pageimpl, Pageable pageable) {
        List<D> listConverted = new ArrayList<>();
        pageimpl.stream().toList().forEach((entity) -> {
            try {
                D instanciaDTOResultado = getClassOfDTO().getDeclaredConstructor().newInstance();
                instanciaDTOResultado.setEntity(entity);
                listConverted.add(instanciaDTOResultado);
            } catch (Throwable noSuchMethodException) {
                throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                        new Object[]{getClassOfDTO().getSimpleName(), noSuchMethodException.getCause()});
            }
        });
        return new PageImpl<>(listConverted,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                pageimpl.getTotalElements());
    }


    protected final List<D> convertirListaADtos(List listaOrigen) {
        List<D> listConverted = new ArrayList<>();
        listaOrigen.stream().toList().forEach((entity) -> {
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
        return listConverted;
    }


}
