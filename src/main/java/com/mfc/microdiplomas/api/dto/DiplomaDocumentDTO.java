package com.mfc.microdiplomas.api.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.domain.model.DiplomaDocument;

public class DiplomaDocumentDTO implements IArqDTO<String, DiplomaDocument> {

    private String id;
    private Long idCliente;
    private String nombreCompleto;
    private String titulacion;
    private String regionOComarca;

    /** campo calculado transient que no está en el modelo (entidad-relacional o document-non-relational) **/
    private String continente;

    private DiplomaDocument diploma = new DiplomaDocument();

    public void setId(String id) {
        this.id = id;
        diploma.setId(id);
    }

    public String getId() {
        return this.id;
    }

    @Override
    public void setEntity(DiplomaDocument diploma) {
        this.diploma = diploma;
        this.id = this.diploma.getId();
        this.idCliente = this.diploma.getIdcustomer();
        this.nombreCompleto = this.diploma.getName();
        this.titulacion = this.diploma.getTitulo();
        this.regionOComarca = this.diploma.getRegion();
    }

    @Override
    @JsonIgnore
    public DiplomaDocument getEntity() {
        return this.diploma;
    }

    public Long getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
        diploma.setIdcustomer(idCliente);
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        diploma.setName(nombreCompleto);
    }

    public String getTitulacion() {
        return this.titulacion;
    }

    public void setTitulacion(String titulacion) {
        this.titulacion = titulacion;
        diploma.setTitulo(titulacion);
    }

    public String getRegionOComarca() {
        return this.regionOComarca;
    }

    public void setRegionOComarca(String regionOComarca) {
        this.regionOComarca = regionOComarca;
        diploma.setRegion(regionOComarca);
    }

    public String getContinente() {
        return this.continente;
    }

    public void setContinente(String continente) {
        this.continente = continente;
    }

}
