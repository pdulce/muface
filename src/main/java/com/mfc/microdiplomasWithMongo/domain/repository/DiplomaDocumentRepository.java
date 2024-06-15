package com.mfc.microdiplomasWithMongo.domain.repository;

import com.mfc.microdiplomasWithMongo.domain.model.DiplomaM;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiplomaDocumentRepository extends MongoRepository<DiplomaM, String> {

    //implementar HQL


}
