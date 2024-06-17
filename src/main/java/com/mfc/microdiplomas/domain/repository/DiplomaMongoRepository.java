package com.mfc.microdiplomas.domain.repository;

import com.mfc.microdiplomas.domain.model.DiplomaDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiplomaMongoRepository extends MongoRepository<DiplomaDocument, Long> {

}
