package com.mfc.microdiplomasWithMongo.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "diplomas")
public class DiplomaM {

    @Id
    private String id;

    private Long idcustomer;

    private String name;

    private String titulo;

    private String region;



}
