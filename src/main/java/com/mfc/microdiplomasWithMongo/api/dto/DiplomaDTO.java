package com.mfc.microdiplomasWithMongo.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mfc.infra.dto.ArqAbstractDTO;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DiplomaDTO extends ArqAbstractDTO {

    private Long id;

    private Long idCliente;

    private String nombreCompleto;

    private String titulacion;

    private String regionOComarca;

    /** campo calculado transiet que no está en el modelo (entidad-relacional o document-non-relational) **/
    private String continente;

    @JsonIgnore
    private static Map<String, String> mapaConversion;

    static {
        // Keys: los miembros del DTO
        // Values: los miembros del Entity
        mapaConversion = new HashMap<>();
        mapaConversion.put("id", "id");
        mapaConversion.put("idCliente", "idcustomer");
        mapaConversion.put("nombreCompleto", "name");
        mapaConversion.put("titulacion", "titulo");
        mapaConversion.put("regionOComarca", "region");
    }


    public Map<String, String> getMapaConversion() {
        return mapaConversion;
    }

}
