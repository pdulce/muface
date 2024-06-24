package com.mfc.microdiplomas.domain.repository;

import com.mfc.microdiplomas.domain.model.DiplomaDocument;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "arch.repository-type.active", havingValue = "mongo", matchIfMissing = false)
public interface DiplomaMongoRepository extends MongoRepository<DiplomaDocument, String> {

}
