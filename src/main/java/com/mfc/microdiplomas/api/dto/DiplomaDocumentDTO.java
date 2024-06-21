package com.mfc.microdiplomas.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.microdiplomas.domain.model.DiplomaDocument;

public class DiplomaDocumentDTO implements IArqDTO<String, DiplomaDocument> {

    private String id;
    private Long idCliente;
    private String nombreCompleto;
    private String titulacion;
    private String regionOComarca;

    /** campo calculado transient que no está en el modelo (entidad-relacional o document-non-relational) **/
    private String continente;

    //private DiplomaDocument diploma = new DiplomaDocument();

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public void setEntity(DiplomaDocument diploma) {
        this.id = diploma.getId();
        this.idCliente = diploma.getIdcustomer();
        this.nombreCompleto = diploma.getName();
        this.titulacion = diploma.getTitulo();
        this.regionOComarca = diploma.getRegion();
    }

    @Override
    @JsonIgnore
    public DiplomaDocument getEntity() {
        DiplomaDocument diploma = new DiplomaDocument();
        diploma.setId(this.id);
        diploma.setIdcustomer(this.idCliente);
        diploma.setName(this.nombreCompleto);
        diploma.setRegion(this.regionOComarca);
        return diploma;
    }

    @Override
    public void actualizarEntidad(DiplomaDocument entity) {
        setEntity(entity);
    }

}
