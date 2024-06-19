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
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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
            // extraer todos los entities de este dto:
            List<String> entidadesEnDto = getEntitiesOfDTO(entityDto);
            for (String entityName : entidadesEnDto) {
                ArqPortRepository<Object, ID> commonRepository = (ArqPortRepository<Object, ID>)
                        this.commonRepositories.get(entityName);
                try {
                    Object entity = ArqAbstractDTO.convertToEntity(entityDto, commonRepository.getClassOfEntity());
                    commonRepository.save(entity);
                    ArqAbstractDTO.incluirEnDTO(entity, entityDtoResultado);
                    this.registrarEvento(entity, ArqEvent.EVENT_TYPE_CREATE);
                    String info = messageSource.getMessage(ArqConstantMessages.CREATED_OK,
                            new Object[]{this.getCollectionName(commonRepository)}, new Locale("es"));
                    logger.info(info);
                } catch (ConstraintViolationException ctExc) {
                    throw ctExc;
                } catch (Throwable exc) {
                    String error = messageSource.getMessage(ArqConstantMessages.CREATED_KO,
                            new Object[]{this.getCollectionName(commonRepository), exc.getCause()},
                            new Locale("es"));
                    logger.error(error);
                    throw new ArqBaseOperationsException(ArqConstantMessages.CREATED_KO,
                            new Object[]{this.getCollectionName(commonRepository), exc.getCause()});
                }
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
            // extraer todos los entities de este dto:
            List<String> entidadesEnDto = getEntitiesOfDTO(entityDto);
            for (String entityName : entidadesEnDto) {
                ArqPortRepository<Object, ID> commonRepository = (ArqPortRepository<Object, ID>)
                        this.commonRepositories.get(entityName);
                try {
                    Object entity = ArqAbstractDTO.convertToEntity(entityDto, commonRepository.getClassOfEntity());
                    ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
                    this.buscarPorId(id);
                    commonRepository.save(entity);
                    ArqAbstractDTO.incluirEnDTO(entity, entityDtoResultado);
                    this.registrarEvento(entity, ArqEvent.EVENT_TYPE_UPDATE);
                    String info = messageSource.getMessage(ArqConstantMessages.UPDATED_OK,
                            new Object[]{this.getCollectionName(commonRepository)}, new Locale("es"));
                    logger.info(info);
                } catch (ConstraintViolationException ctExc) {
                    throw ctExc;
                } catch (Throwable exc) {
                    String error = messageSource.getMessage(ArqConstantMessages.UPDATED_KO,
                            new Object[]{this.getCollectionName(commonRepository), exc.getCause()},
                            new Locale("es"));
                    logger.error(error);
                    throw new ArqBaseOperationsException(ArqConstantMessages.UPDATED_KO,
                            new Object[]{this.getCollectionName(commonRepository), exc.getCause()});
                }
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
    public int borrarEntidad(ID idPral) {
        try {
            D entityDto = buscarPorId(idPral);
            // extraer todos los entities de este dto:
            List<String> entidadesEnDto = getEntitiesOfDTO(entityDto);
            for (String entityName : entidadesEnDto) {
                ArqPortRepository<?, ID> commonRepository = this.commonRepositories.get(entityName);
                try {
                    Object entity = ArqAbstractDTO.convertToEntity(entityDto, commonRepository.getClassOfEntity());
                    ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
                    this.buscarPorId(id);
                    ArqPortRepository<Object, ID> castedRepository = (ArqPortRepository<Object, ID>) commonRepository;
                    castedRepository.delete(entity);
                    this.registrarEvento(entity, ArqEvent.EVENT_TYPE_DELETE);
                    String info = messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                            new Object[]{this.getCollectionName(commonRepository)}, new Locale("es"));
                    logger.info(info);
                } catch (Throwable exc) {
                    String error = messageSource.getMessage(ArqConstantMessages.DELETED_KO,
                            new Object[]{this.getCollectionName(commonRepository), exc.getCause()},
                            new Locale("es"));
                    logger.error(error);
                    throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                            new Object[]{this.getCollectionName(commonRepository), exc.getCause()});
                }
            }
            return 1;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
        }
    }


    @Override
    @Transactional
    public int borrarEntidad(D entityDto) {
        try {
            Class<D> dtoClass = (Class<D>) entityDto.getClass();
            D entityDtoResultado = dtoClass.getDeclaredConstructor().newInstance();
            // extraer todos los entities de este dto:
            List<String> entidadesEnDto = getEntitiesOfDTO(entityDto);
            for (String entityName : entidadesEnDto) {
                ArqPortRepository<?, ID> commonRepository = this.commonRepositories.get(entityName);
                try {
                    Object entity = ArqAbstractDTO.convertToEntity(entityDto, commonRepository.getClassOfEntity());
                    ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
                    this.buscarPorId(id);
                    ArqPortRepository<Object, ID> castedRepository = (ArqPortRepository<Object, ID>) commonRepository;
                    castedRepository.delete(entity);
                    ArqAbstractDTO.incluirEnDTO(entity, entityDtoResultado);
                    this.registrarEvento(entity, ArqEvent.EVENT_TYPE_DELETE);
                    String info = messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                            new Object[]{this.getCollectionName(commonRepository)}, new Locale("es"));
                    logger.info(info);
                } catch (Throwable exc) {
                    String error = messageSource.getMessage(ArqConstantMessages.DELETED_KO,
                            new Object[]{this.getCollectionName(commonRepository), exc.getCause()},
                            new Locale("es"));
                    logger.error(error);
                    throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                            new Object[]{this.getCollectionName(commonRepository), exc.getCause()});
                }
            }
            return 1;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{entityDto.getClass().getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    @Override
    @Transactional
    public int borrarEntidades(List<D> entities) {
        try{
            entities.forEach((entityDTO) -> {
                borrarEntidad(entityDTO);
            });
            String info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                    new Object[]{"entidades de dto"}, new Locale("es"));
            logger.info(info);
            return entities.size();
        } catch (ArqBaseOperationsException notExists) {
            throw notExists;
        } catch (Throwable exc) {
            throw exc;
        }
    }

    @Override
    @Transactional
    public void borrarTodos() {
        try {
            D entityDto = dtoClass.getDeclaredConstructor().newInstance();
            // extraer todos los entities de este dto:
            List<String> entidadesEnDto = getEntitiesOfDTO(entityDto);
            for (String entityName : entidadesEnDto) {
                ArqPortRepository<Object, ID> commonRepository = (ArqPortRepository<Object, ID>)
                        this.commonRepositories.get(entityName);
                try {
                    commonRepository.deleteAll();
                    this.registrarEvento(entityDto, ArqEvent.EVENT_TYPE_DELETE);
                    String info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                            new Object[]{this.getCollectionName(commonRepository)}, new Locale("es"));
                    logger.info(info);
                } catch (Throwable exc) {
                    String error = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_KO,
                            new Object[]{this.getCollectionName(commonRepository), exc.getCause()},
                            new Locale("es"));
                    logger.error(error);
                    throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                            new Object[]{this.getCollectionName(commonRepository), exc.getCause()});
                }
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    @Override
    public List<D> buscarTodos() {
        try {
            List<D> resultado = new ArrayList<>();
            ArqPortRepository<Object, ID> commonRepositoryPral = getRepositorioDePrincipal();
            commonRepositoryPral.findAll().forEach((objEntityPral) -> {
                try {
                    D instanciaDTOResultado = dtoClass.getDeclaredConstructor().newInstance();
                    ArqAbstractDTO.incluirEnDTO(objEntityPral, instanciaDTOResultado);
                    resultado.add(instanciaDTOResultado);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                         | InvocationTargetException noSuchMethodException) {
                    throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                            new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
                }
            });
            return resultado;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    @Override
    public D buscarPorId(ID id) {

        try {
            D entityDtoResultado = dtoClass.getDeclaredConstructor().newInstance();
            ArqPortRepository<Object, ID> commonRepositoryPral = getRepositorioDePrincipal();
            Optional<?> optionalT = commonRepositoryPral.findById(id);
            if (optionalT.isPresent()) {
                ArqAbstractDTO.incluirEnDTO(optionalT.get(), entityDtoResultado);
                return entityDtoResultado;
            } else {
                throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{this.getCollectionName(commonRepositoryPral), id});
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
        }
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

    // TODO:

    @Override
    public List<D> buscarCoincidenciasEstricto(D filterObject) {
        try {
            List<D> resultado = new ArrayList<>();
            ArqPortRepository<Object, ID> commonRepositoryPral = getRepositorioDePrincipal();
            Object entity = ArqAbstractDTO.convertToEntity(filterObject, commonRepositoryPral.getClassOfEntity());
            commonRepositoryPral.findByExampleNotStricted(entity).forEach((objEntityPral) -> {
                try {
                    D instanciaDTOResultado = dtoClass.getDeclaredConstructor().newInstance();
                    ArqAbstractDTO.incluirEnDTO(objEntityPral, instanciaDTOResultado);
                    resultado.add(instanciaDTOResultado);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                         | InvocationTargetException noSuchMethodException) {
                    throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                            new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
                }
            });
            return resultado;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    @Override
    public List<D> buscarCoincidenciasNoEstricto(D filterObject) {
        try {
            List<D> resultado = new ArrayList<>();
            ArqPortRepository<Object, ID> commonRepositoryPral = getRepositorioDePrincipal();
            Object entity = ArqAbstractDTO.convertToEntity(filterObject, commonRepositoryPral.getClassOfEntity());
            commonRepositoryPral.findByExampleNotStricted(entity).forEach((objEntityPral) -> {
                try {
                    D instanciaDTOResultado = dtoClass.getDeclaredConstructor().newInstance();
                    ArqAbstractDTO.incluirEnDTO(objEntityPral, instanciaDTOResultado);
                    resultado.add(instanciaDTOResultado);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                         | InvocationTargetException noSuchMethodException) {
                    throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                            new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
                }
            });
            return resultado;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException
                 | InvocationTargetException noSuchMethodException) {
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{dtoClass.getSimpleName(), noSuchMethodException.getCause()});
        }
    }

    private ArqPortRepository<Object, ID> getRepositorioDePrincipal()
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        IArqDTO instanceNew = dtoClass.getDeclaredConstructor().newInstance();
        String entidadPrincipalJPA = instanceNew.getJPAEntidadPrincipal();
        String entidadPrincipalMongo = instanceNew.getMongoEntidadPrincipal();

        return (ArqPortRepository<Object, ID>) (this.commonRepositories.get(entidadPrincipalJPA) != null
                        ? this.commonRepositories.get(entidadPrincipalJPA) :
                        this.commonRepositories.get(entidadPrincipalMongo));
    }

    private List<String> getEntitiesOfDTO(IArqDTO dto)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        IArqDTO instanceNew = dtoClass.getDeclaredConstructor().newInstance();
        String entidadPrincipalJPA = instanceNew.getJPAEntidadPrincipal();
        String entidadPrincipalMongo = instanceNew.getMongoEntidadPrincipal();

        return this.commonRepositories.get(entidadPrincipalJPA) != null ? instanceNew.getModelJPAEntities()
                                                                        : instanceNew.getModelMongoEntities();
    }


}
