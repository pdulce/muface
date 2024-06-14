package com.mfc.infra.output.adapter;

import com.mfc.infra.output.port.ArqMongoServicePort;
import com.mfc.infra.utils.ArqConversionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public abstract class ArqMongoServiceAdapter<T> implements ArqMongoServicePort<T> {
    Logger logger = LoggerFactory.getLogger(ArqMongoServiceAdapter.class);

    @Autowired
    MongoRepository<T, String> repository;

    @Override
    public void saveReg(LinkedHashMap deserialized, Class<T> entityClass) {
        T document = ArqConversionUtils.jsonStringToObject(ArqConversionUtils.map2Jsonstring(deserialized), entityClass);
        this.repository.save(document);
    }

    @Override
    public void deleteReg(String id) {
        this.repository.deleteById(id);
    }

    @Override
    public T findById(String id) {
        Optional<T> searched = this.repository.findById(id);
        return searched.isPresent() ? searched.get() : null;
    }

    @Override
    public List<T> findAll() {
        return this.repository.findAll();
    }

}