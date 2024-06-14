package com.mfc.microdiplomas.api.dto;

import com.mfc.infra.dto.ArqAbstractDTO;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DiplomaDTO extends ArqAbstractDTO {

    private Long indentificador;

    private Long idCliente;

    private String nombreCompleto;

    private String titulacion;

    private String regionOComarca;
    public DiplomaDTO(){}

    public Map<String, String> getMapaConversion() {
        Map<String, String> mapaConversion = new HashMap<>();
        mapaConversion.put("indentificador", "id");
        mapaConversion.put("idCliente", "idcustomer");
        mapaConversion.put("nombreCompleto", "name");
        mapaConversion.put("titulacion", "titulo");
        mapaConversion.put("regionOComarca", "region");
        return new HashMap<>();
    }


}
