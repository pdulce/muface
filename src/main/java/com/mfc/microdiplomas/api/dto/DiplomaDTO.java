package com.mfc.microdiplomas.api.dto;


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

    private Diploma diploma;

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
    }

    @Override
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
