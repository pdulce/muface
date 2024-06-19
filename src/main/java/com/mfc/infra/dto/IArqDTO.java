package com.mfc.infra.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IArqDTO extends Serializable {

    Map<String, String> getMapaConversion();
    Object getId();
    List<String> getModelMongoEntities();

    List<String> getModelJPAEntities();

    String getMongoEntidadPrincipal();

    String getJPAEntidadPrincipal();

}
