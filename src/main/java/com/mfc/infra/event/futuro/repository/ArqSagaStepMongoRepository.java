package com.mfc.infra.event.futuro.repository;

import com.mfc.infra.event.ArqSagaStepEventDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ConditionalOnProperty(name = "arch.repository-type.active", havingValue = "mongo", matchIfMissing = false)
public interface ArqSagaStepMongoRepository extends MongoRepository<ArqSagaStepEventDocument, String> {
    List<ArqSagaStepEventDocument> findByApplicationIdAndSagaAndStepId(String applicationId, String almacen,
                                                                            String entityId);
    List<ArqSagaStepEventDocument> findByApplicationIdAndSaga(String applicationId, String almacen);
    List<ArqSagaStepEventDocument> findByApplicationId(String applicationId);
    List<ArqSagaStepEventDocument> findAll();


}
