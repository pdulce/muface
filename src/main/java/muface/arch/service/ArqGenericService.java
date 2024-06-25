package muface.arch.service;

import muface.arch.command.IArqDTOMapper;
import muface.arch.configuration.ArqConfigProperties;
import muface.arch.configuration.ArqSessionInterceptor;
import muface.arch.command.IArqDTO;
import muface.arch.event.ArqEvent;
import muface.arch.event.futuro.publishers.ArqCommandEventPublisherPort;
import muface.arch.exceptions.ArqBaseOperationsException;
import muface.arch.exceptions.NotExistException;
import muface.arch.repository.ArqPortRepository;
import muface.arch.utils.ArqConstantMessages;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.*;

import java.io.Serializable;
import java.util.*;

@Transactional
public abstract class ArqGenericService<D extends IArqDTO, ID> implements ArqServicePort<D, ID> {
    Logger logger = LoggerFactory.getLogger(ArqGenericService.class);
    @Autowired
    ArqConfigProperties arqConfigProperties;

    @Autowired(required = false)
    ArqCommandEventPublisherPort arqCommandEventPublisherPort;

    @Autowired
    private IArqDTOMapper<Serializable, D> mapper;

    @Autowired
    protected ApplicationContext applicationContext;

    public abstract String getRepositoryEntityOfDTO();

    @Autowired
    MessageSource messageSource;

    private String getClassOfDTO() {
        return mapper.newInstance().getEntity().getClass().getSimpleName();
    }

    protected ArqPortRepository<Object, ID> getRepository() {
        String entityClassName = mapper.newInstance().getEntity().getClass().getName();
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

    private String fabricarIdUnico(String applicationId, String almacen, Object id) {
        return applicationId + "-" + almacen + "-" + (id.toString());
    }

    private void registrarEvento(Serializable entity, String eventType, ID id) {
        if (entity != null && arqConfigProperties.isEventBrokerActive()) {
            String applicationId = (String) ArqSessionInterceptor.getCurrentSession().getAttribute("applicationId");
            String sessionId = (String) ArqSessionInterceptor.getCurrentSession().getAttribute("sessionId");
            String traceId = (String) ArqSessionInterceptor.getCurrentSession().getAttribute("sessionId");
            String almacen = getClassOfDTO();
            String idUnique = fabricarIdUnico(applicationId, almacen, id);
            ArqEvent eventArch = new ArqEvent(applicationId, almacen, eventType, sessionId, traceId, idUnique, entity);
            arqCommandEventPublisherPort.publish(ArqEvent.TOPIC_AUDITORIAS, eventArch);
        }
    }
    private void registrarEventos(List<Object> entities, String eventType) {
        entities.forEach((entity) -> {
            D dto = mapper.map((Serializable) entity);
            registrarEvento((Serializable) entity, eventType, (ID) dto.getId());
        });
    }


    @Override
    @Transactional
    public D insertar(D entityDto) {
        D dto;
        try {
            ArqPortRepository<Object, ID> commandRepo = getRepository();
            Serializable entidad = (Serializable) entityDto.getEntity();
            Serializable entityInserted = (Serializable) commandRepo.save(entidad);
            dto = mapper.map(entityInserted);
            this.registrarEvento(entityInserted, ArqEvent.EVENT_TYPE_CREATE, (ID) entityDto.getId());
            String info = messageSource.getMessage(ArqConstantMessages.CREATED_OK,
                    new Object[]{getClassOfDTO()}, new Locale("es"));
            logger.info(info);
        } catch (ConstraintViolationException ctExc) {
            throw ctExc;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.CREATED_KO,
                    new Object[]{getClassOfDTO(), exc.getCause()},
                    new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.CREATED_KO,
                    new Object[]{getClassOfDTO(), exc.getCause()});
        }
        return dto;
    }

    @Override
    @Transactional
    public D actualizar(D entityDto) {
        try {
            ArqPortRepository<Object, ID> commandRepo = getRepository();
            Optional<?> optionalT = commandRepo.findById((ID) entityDto.getId());
            if (optionalT.isPresent()) {
                Serializable searchedInBBDD = (Serializable) optionalT.orElse(null);
                entityDto.actualizarEntidad(searchedInBBDD); //actualiza los campos que no son ids
                Serializable updated = (Serializable) commandRepo.save(searchedInBBDD);
                this.registrarEvento(updated, ArqEvent.EVENT_TYPE_UPDATE, (ID) entityDto.getId());
                String info = messageSource.getMessage(ArqConstantMessages.UPDATED_OK,
                        new Object[]{getClassOfDTO()}, new Locale("es"));
                logger.info(info);
            } else {
                throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{getClassOfDTO(), (ID) entityDto.getId()});
            }
        } catch (ConstraintViolationException | NotExistException ctExc) {
            throw ctExc;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.UPDATED_KO,
                    new Object[]{getClassOfDTO(), exc.getCause()},
                    new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.UPDATED_KO,
                    new Object[]{getClassOfDTO(), exc.getCause()});
        }
        return entityDto;
    }

    @Override
    @Transactional
    public String borrarEntidad(ID id) {
        if (id == null) {
            throw new NotExistException(ArqConstantMessages.ERROR_BAD_REQUEST,
                    new Object[]{getClassOfDTO(), "id: <null>"});
        }
        String info = "";
        try {
            ArqPortRepository<Object, ID> commandRepo = getRepository();
            Optional<?> optionalT = commandRepo.findById(id);
            if (optionalT.isPresent()) {
                Serializable entity = (Serializable) optionalT.orElse(null);
                commandRepo.delete(entity);
                this.registrarEvento(entity, ArqEvent.EVENT_TYPE_DELETE, id);
                info = messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                        new Object[]{getClassOfDTO()}, LocaleContextHolder.getLocale());
                logger.info(messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                        new Object[]{getClassOfDTO()}, new Locale("es")));
            } else {
                throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{getClassOfDTO(), id});
            }
        } catch (ConstraintViolationException | NotExistException ctExc) {
            throw ctExc;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_KO,
                    new Object[]{getClassOfDTO(), exc.getCause()},
                    LocaleContextHolder.getLocale());
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{getClassOfDTO(), exc.getCause()});
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
    public String borrarEntidades(D filter) {
        return borrarEntidades(this.buscarCoincidenciasEstricto(filter));
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
                    new Object[]{entities.size(), getClassOfDTO()},
                    LocaleContextHolder.getLocale());
            logger.info(messageSource.getMessage(ArqConstantMessages.DELETED_LIST_OK,
                    new Object[]{getClassOfDTO()}, new Locale("es")));
            return info;
        } catch (Throwable exc) {
            throw exc;
        }
    }

    @Override
    @Transactional
    public String borrarTodos() {
        String info = "";
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        List<Object> registros = commandRepo.findAll();
        if (registros.isEmpty()) {
            info = messageSource.getMessage(ArqConstantMessages.NOTHING_TO_DELETE, null,
                    LocaleContextHolder.getLocale());
            logger.info(info);
        } else {
            try {
                commandRepo.deleteAll();
                this.registrarEventos(registros, ArqEvent.EVENT_TYPE_DELETE);
                info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                        new Object[]{getClassOfDTO()}, LocaleContextHolder.getLocale());
                logger.info(messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                        new Object[]{getClassOfDTO()}, new Locale("es")));
            } catch (Throwable exc) {
                String error = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_KO,
                        new Object[]{getClassOfDTO(), exc.getCause()},
                        new Locale("es"));
                logger.error(error);
                throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                        new Object[]{getClassOfDTO(), exc.getCause()});
            }
        }
        return info;
    }

    @Override
    public List<D> buscarTodos() {
        List<Object> resultado = this.getRepository().findAll();
        return convertirListaEntitiesADtos(resultado);
    }
    @Override
    public List<D> buscarTodosConOrdenacion(Sort sort) {
        if (sort == null) {
            logger.error("Parámetro sort es nulo");
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{"Parámetro sort es nulo"});
        }
        List<Object> resultado = this.getRepository().findAll(sort);
        return convertirListaEntitiesADtos(resultado);
    }

    @Override
    public D buscarPorId(ID id) {
        if (id == null) {
            throw new NotExistException(ArqConstantMessages.ERROR_BAD_REQUEST,
                    new Object[]{getClassOfDTO(), "id: <null>"});
        }
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        Optional<?> optionalT = commandRepo.findById(id);
        if (optionalT.isPresent()) {
            Serializable entity = (Serializable) optionalT.orElse(null);
            D dto = mapper.map(entity);
            return dto;
        } else {
            throw new NotExistException(ArqConstantMessages.RECORD_NOT_FOUND,
                    new Object[]{getClassOfDTO(), id});
        }
    }

    @Override
    public List<D> buscarPorIds(List<ID> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new NotExistException(ArqConstantMessages.ERROR_BAD_REQUEST,
                    new Object[]{getClassOfDTO(), "ids: <null or empty list>"});
        }
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        List<Object> resultado = commandRepo.findByIds(ids);
        return convertirListaEntitiesADtos(resultado);
    }

    @Override
    public List<D> buscarCoincidenciasEstricto(D filterObject) {
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        List<Object> resultadoEntities = commandRepo.findByExampleStricted(filterObject.getEntity());
        return convertirListaEntitiesADtos(resultadoEntities);
    }

    @Override
    public List<D> buscarCoincidenciasNoEstricto(D filterObject) {
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        List<Object> resultadoEntities = commandRepo.findByExampleNotStricted(filterObject.getEntity());
        return convertirListaEntitiesADtos(resultadoEntities);
    }

    /*** Consultas paginadas ***/
    @Override
    public Page<D> buscarCoincidenciasEstrictoPaginados(D filterObject, Pageable pageable) {
        Pageable newPageable = mapearCamposOrdenacionDeEntidad(filterObject, pageable);
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
        Pageable newPageable = mapearCamposOrdenacionDeEntidad(filterObject, pageable);
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
        Pageable newPageable = mapearCamposOrdenacionDeEntidad(mapper.newInstance(), pageable);
        if (newPageable == null) {
            logger.error("Parámetro pageable es nulo");
            throw new ArqBaseOperationsException(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                    new Object[]{"Parámetro pageable es nulo"});
        }
        ArqPortRepository<Object, ID> commandRepo = getRepository();
        Page<Object> resultado = commandRepo.findAllPaginated(newPageable);
        return convertirAPageOfDtos(resultado, newPageable);
    }

    protected final Page<D> convertirAPageOfDtos(Page pageimpl, Pageable pageable) {
        List<D> listConverted = new ArrayList<>();
        pageimpl.stream().toList().forEach((entity) -> {
            D dto = mapper.map((Serializable) entity);
            listConverted.add(dto);
        });
        return new PageImpl<>(listConverted,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                pageimpl.getTotalElements());
    }


    protected final List<D> convertirListaEntitiesADtos(List listaOrigen) {
        List<D> listConverted = new ArrayList<>();
        listaOrigen.stream().toList().forEach((entity) -> {
            D dto = mapper.map((Serializable) entity);
            listConverted.add(dto);
        });
        return listConverted;
    }

    protected static Pageable mapearCamposOrdenacionDeEntidad(IArqDTO dto, Pageable pageable) {
        if (pageable.getSort() == null || pageable.getSort().isEmpty()) {
            return pageable;
        }
        Sort originalSort = pageable.getSort();
        Sort newSort = Sort.unsorted();

        for (Sort.Order order : originalSort) {
            String property = order.getProperty();
            String transformedProperty = dto.getInnerOrderField(property);
            newSort = newSort.and(Sort.by(order.getDirection(), transformedProperty));
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), newSort);
    }

}
