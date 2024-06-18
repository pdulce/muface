package com.mfc.microdiplomas.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mfc.infra.dto.ArqAbstractDTO;
import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.domain.model.DiplomaDocument;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DiplomaDTO extends ArqAbstractDTO {

    private String id;

    private Long idCliente;

    private String nombreCompleto;

    private String titulacion;

    private String regionOComarca;

    /** campo calculado transient que no está en el modelo (entidad-relacional o document-non-relational) **/
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

    @Override
    public List<String> getModelJPAEntities() {
        List<String> ar = new ArrayList<>();
        ar.add(Diploma.class.getName());
        return ar;
    }

    @Override
    public List<String> getModelMongoEntities() {
        List<String> ar = new ArrayList<>();
        ar.add(DiplomaDocument.class.getName());
        return ar;
    }

    public String getJPAEntidadPrincipal() {
        return Diploma.class.getName();
    }

    public String getMongoEntidadPrincipal() {
        return DiplomaDocument.class.getName();
    }


}
