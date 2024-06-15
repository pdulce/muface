package com.mfc.infra.output.adapter;

import com.mfc.infra.dto.ArqAbstractDTO;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.exceptions.ArqNotExistException;
import com.mfc.infra.output.port.ArqServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public abstract class ArqMongoServiceAdapter<T, D extends IArqDTO, ID> implements ArqServicePort<T, D, ID> {
    Logger logger = LoggerFactory.getLogger(ArqMongoServiceAdapter.class);

    protected abstract MongoRepository<T, String> getRepository();

    @Autowired
    MongoOperations mongoOperations;

    // Implementación de los métodos de ArqServicePort

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

    @Override
    public D crear(D entityDto) {
        T entityToSave = ArqAbstractDTO.convertToEntity(entityDto, getClassOfEntity());
        entityToSave = getRepository().save(entityToSave);
        entityDto = ArqAbstractDTO.convertToDTO(entityToSave, getClassOfDTO());
        return entityDto;
    }

    @Override
    public D actualizar(D entityDto) {
        T entityToUpdate = ArqAbstractDTO.convertToEntity(entityDto, getClassOfEntity());
        entityToUpdate = getRepository().save(entityToUpdate);
        entityDto = ArqAbstractDTO.convertToDTO(entityToUpdate, getClassOfDTO());
        return entityDto;
    }

    @Override
    public int borrar(D entity) {
        Query deleteQuery = new Query();
        Criteria criteria = Criteria.where("_id").is(entity.getId());
        deleteQuery.addCriteria(criteria);
        mongoOperations.remove(deleteQuery, entity.getClass());
        return 1; // asumiendo que siempre se borra exactamente un elemento
    }

    @Override
    public int borrar(List<D> entities) {
        List<ID> ids = new ArrayList<>();
        for (D entity : entities) {
            ids.add((ID) entity.getId());
        }
        Query deleteQuery = new Query();
        Criteria criteria = Criteria.where("_id").in(ids);
        deleteQuery.addCriteria(criteria);
        mongoOperations.remove(deleteQuery, entities.get(0).getClass());
        return entities.size();
    }

    @Override
    public void borrar() {
        mongoOperations.dropCollection(getDocumentEntityClassname());
    }

    @Override
    public D buscarPorId(ID id) {
        Optional<T> result = getRepository().findById((String) id);
        D item = result.isPresent() ? ArqAbstractDTO.convertToDTO(result.isPresent(), getClassOfDTO()) : null;
        if (item == null) {
            logger.info("buscarPorId no localizó el id: " + id);
            ArqNotExistException e = new ArqNotExistException();
            e.setMsgError("id: " + id);
            RuntimeException exc = new RuntimeException(e);
            throw exc;
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
        // Implementación según tus necesidades específicas
        return Collections.emptyList(); // Ejemplo
    }

    @Override
    public List<D> buscarCoincidenciasNoEstricto(D filterObject) {
        // Implementación según tus necesidades específicas
        return Collections.emptyList(); // Ejemplo
    }

    @Override
    public String getDocumentEntityClassname() {
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass())
                .getActualTypeArguments()[0];
        return entityClass.getSimpleName().toLowerCase();
    }

    // Métodos de conversión entre DTO y Entity

    protected List<D> convertToDTOList(List<T> entities) {
        List<D> dtos = new ArrayList<>();
        for (T entity : entities) {
            dtos.add(ArqAbstractDTO.convertToDTO(entity, getClassOfDTO()));
        }
        return dtos;
    }

}
