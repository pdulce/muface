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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
public abstract class ArqServiceRelationalDBAdapter<T, D extends IArqDTO, ID> extends ArqAbstractService<T, D, ID>
        implements ArqServicePort<T, D, ID> {
    Logger logger = LoggerFactory.getLogger(ArqServiceRelationalDBAdapter.class);

    protected abstract JpaRepository<T, ID> getRepository();

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public D crear(D entityDto) {
        try {
            T entity = ArqAbstractDTO.convertToEntity(entityDto, getClassOfEntity());
            this.getRepository().save(entity);
            entityDto = ArqAbstractDTO.convertToDTO(entity, getClassOfDTO());
            super.registrarEvento(entity, ArqEvent.EVENT_TYPE_CREATE);
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
            if (!this.getRepository().findById(id).isPresent()) {
                throw new ArqBaseOperationsException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{getClassOfEntity().getSimpleName(), id});
            }
            this.getRepository().save(entity);
            super.registrarEvento(entity, ArqEvent.EVENT_TYPE_UPDATE);
            return ArqAbstractDTO.convertToDTO(entity, getClassOfDTO());
        } catch (ArqBaseOperationsException notExists) {
            throw notExists;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.UPDATED_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.UPDATED_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()});
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public int borrar(D entityDto) {
        try {
            T entity = ArqAbstractDTO.convertToEntity(entityDto, getClassOfEntity());
            ID id = (ID) ArqConversionUtils.convertToMap(entity).get("id");
            if (!this.getRepository().findById(id).isPresent()) {
                throw new ArqBaseOperationsException(ArqConstantMessages.RECORD_NOT_FOUND,
                        new Object[]{getClassOfEntity().getSimpleName(), id});
            }
            this.getRepository().delete(entity);
            String info = messageSource.getMessage(ArqConstantMessages.DELETED_OK,
                    new Object[]{getClassOfEntity().getSimpleName()}, new Locale("es"));
            logger.info(info);
            super.registrarEvento(entity, ArqEvent.EVENT_TYPE_DELETE);
            return 1;
        } catch (ArqBaseOperationsException notExists) {
            throw notExists;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()});
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public int borrar(List<D> entities) {
        try {
            AtomicInteger counter = new AtomicInteger();
            entities.forEach((entityDTO) -> {
                this.borrar(entityDTO);
                counter.getAndIncrement();
                T entity = ArqAbstractDTO.convertToEntity(entityDTO, getClassOfEntity());
                super.registrarEvento(entity, ArqEvent.EVENT_TYPE_DELETE);
            });
            String info = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_OK,
                    new Object[]{getClassOfEntity().getSimpleName()}, new Locale("es"));
            logger.info(info);
            return counter.get();
        } catch (ArqBaseOperationsException notExists) {
            throw notExists;
        } catch (Throwable exc) {
            String error = messageSource.getMessage(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()}, new Locale("es"));
            logger.error(error);
            throw new ArqBaseOperationsException(ArqConstantMessages.DELETED_ALL_KO,
                    new Object[]{getClassOfEntity().getSimpleName(), exc.getCause()});
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional
    public void borrar() {
        try{
            List<T> entities = new ArrayList<>();
            buscarTodos().forEach((entityDTO) -> {
                T entity = ArqAbstractDTO.convertToEntity(entityDTO, getClassOfEntity());
                entities.add(entity);
            });
            this.getRepository().deleteAll();
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

    @SuppressWarnings("unchecked")
    @Override
    public D buscarPorId(ID id) {
        if (!this.getRepository().findById(id).isPresent()) {
            throw new ArqBaseOperationsException(ArqConstantMessages.RECORD_NOT_FOUND,
                    new Object[]{getClassOfEntity().getSimpleName(), id});
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
