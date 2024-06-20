package com.mfc.microdiplomas.api.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.microdiplomas.domain.model.Diploma;

public class DiplomaDTO implements IArqDTO<Long, Diploma> {

    private Long id;
    private Long idCliente;
    private String nombreCompleto;
    private String titulacion;
    private String regionOComarca;

    /** campo calculado transient que no está en el modelo (entidad-relacional o document-non-relational) **/
    private String continente;

    private Diploma diploma = new Diploma();

    public void setId(Long id) {
        this.id = id;
        diploma.setId(id);
    }

    public Long getId() {
        return this.id;
    }

    @Override
    public void setEntity(Diploma diploma) {
        this.diploma = diploma;
        this.id = this.diploma.getId();
        this.idCliente = this.diploma.getIdcustomer();
        this.nombreCompleto = this.diploma.getName();
        this.titulacion = this.diploma.getTitulo();
        this.regionOComarca = this.diploma.getRegion();
    }

    @Override
    @JsonIgnore
    public Diploma getEntity() {
        return this.diploma;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
        diploma.setIdcustomer(id);
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        diploma.setName(nombreCompleto);
    }

    public String getTitulacion() {
        return titulacion;
    }

    public void setTitulacion(String titulacion) {
        this.titulacion = titulacion;
        diploma.setTitulo(titulacion);
    }

    public String getRegionOComarca() {
        return regionOComarca;
    }

    public void setRegionOComarca(String regionOComarca) {
        this.regionOComarca = regionOComarca;
        diploma.setRegion(regionOComarca);
    }

    public String getContinente() {
        return continente;
    }

    public void setContinente(String continente) {
        this.continente = continente;
    }

}
