package com.mfc.infra.event.futuro.repository;

import com.mfc.infra.event.ArqEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArqEventMongoRepository extends MongoRepository<ArqEventDocument, String> {
    List<ArqEventDocument> findByApplicationIdAndAlmacenAndEntityId(String applicationId, String almacen, String entityId);
    List<ArqEventDocument> findByApplicationIdAndAlmacen(String applicationId, String almacen);
    List<ArqEventDocument> findByApplicationId(String applicationId);
    List<ArqEventDocument> findAll();


}
