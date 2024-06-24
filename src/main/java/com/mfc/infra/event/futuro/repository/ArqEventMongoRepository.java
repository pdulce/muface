package com.mfc.infra.event.futuro.repository;

import com.mfc.infra.event.ArqAuditoriaDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ConditionalOnProperty(name = "arch.repository-type.active", havingValue = "mongo", matchIfMissing = false)
public interface ArqEventMongoRepository extends MongoRepository<ArqAuditoriaDocument, String> {
    List<ArqAuditoriaDocument> findByApplicationIdAndAlmacenAndEntityId(String applicationId, String almacen, String entityId);
    List<ArqAuditoriaDocument> findByApplicationIdAndAlmacen(String applicationId, String almacen);
    List<ArqAuditoriaDocument> findByApplicationId(String applicationId);
    List<ArqAuditoriaDocument> findAll();


}
