package com.mfc.backend.microdiplomas.api.dto;

import com.mfc.infra.domain.ArqAbstractDTO;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DiplomaDTOArq extends ArqAbstractDTO {

    private Long id;

    private Long idcustomer;

    private String name;

    private String titulo;

    private String region;
    public DiplomaDTOArq(){}

    public Map<String, String> getMapaConversion() {
        return new HashMap<>();
    }

    public DiplomaDTOArq(Long id, Long idcustomer, String name, String titulo, String region) {
        this.id = id;
        this.idcustomer = idcustomer;
        this.name = name;
        this.titulo = titulo;
        this.region = region;
    }


}
