package com.mfc.infra.output.port;

import java.util.LinkedHashMap;
import java.util.List;

public interface ArqMongoServicePort<T> {
    
    void deleteReg(String id);
    void saveReg(LinkedHashMap deserialized, Class<T> entityClass);

    T findById(String id);

    List<T> findAll();



}
