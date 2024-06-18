package com.mfc.infra.dto;

import java.util.List;
import java.util.Map;

public interface IArqDTO {

    Map<String, String> getMapaConversion();
    Object getId();
    List<String> getModelEntities();

    String getEntidadPrincipal();

}
