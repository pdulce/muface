package com.mfc.infra.event;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "auditorias")
@Data
public class ArqAuditoriaDocument {

    @Id
    private String id;

    private String applicationId;
    private String almacen;
    private String entityId;
    private String idEntry;
    private ArqEvent<?> event;
}
