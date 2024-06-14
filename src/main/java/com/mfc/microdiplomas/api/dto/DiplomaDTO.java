package com.mfc.microdiplomas.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mfc.infra.dto.ArqAbstractDTO;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DiplomaDTO extends ArqAbstractDTO {

    private Long identificador;

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
        mapaConversion.put("identificador", "id");
        mapaConversion.put("idCliente", "idcustomer");
        mapaConversion.put("nombreCompleto", "name");
        mapaConversion.put("titulacion", "titulo");
        mapaConversion.put("regionOComarca", "region");
    }
    public DiplomaDTO(){}

    public Map<String, String> getMapaConversion() {
        return mapaConversion;
    }


}
