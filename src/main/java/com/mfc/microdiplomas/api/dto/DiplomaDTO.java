package com.mfc.microdiplomas.api.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.domain.model.FirmaOrganismo;
import com.mfc.microdiplomas.domain.model.Titulacion;

import java.util.*;

public class DiplomaDTO implements IArqDTO<Long, Diploma> {

    private Long id;
    private Long idCliente;
    private String nombreCompleto;
    private String regionOComarca;
    private String titulacion;
    private Map<String, Date> firmas;

    /** campo calculado transient que no está en el modelo (entidad-relacional o document-non-relational) **/
    private String continente;

    private Diploma diploma = new Diploma();

    public DiplomaDTO() {

    }

    @Override
    public void setEntity(Diploma diploma) {
        this.diploma = diploma;
        this.id = this.diploma.getId();
        this.idCliente = this.diploma.getIdcustomer();
        this.nombreCompleto = this.diploma.getName();
        this.titulacion = this.diploma.getTitulacion().getName();
        this.regionOComarca = this.diploma.getRegion();
        this.firmas = new HashMap<>();
        if (this.diploma.getFirmas() != null) {
            this.diploma.getFirmas().forEach((firma) -> {
                this.firmas.put(firma.getOrganismoFirmante(), firma.getFechaEmision());
            });
        }
    }
    @Override
    @JsonIgnore
    public Diploma getEntity() {
        this.diploma = new Diploma();
        this.diploma.setId(this.id);
        this.diploma.setIdcustomer(this.idCliente);
        this.diploma.setName(this.nombreCompleto);
        this.diploma.setRegion(this.regionOComarca);

        if (this.titulacion != null) {
            this.diploma.setTitulacion(new Titulacion());
            this.diploma.getTitulacion().setName(this.titulacion);
        }

        if (this.firmas != null && !this.firmas.isEmpty())  {
            this.diploma.setFirmas(new ArrayList<>());
            Iterator<String> firmantesIterator = this.firmas.keySet().iterator();
            while (firmantesIterator.hasNext()) {
                String firmante = firmantesIterator.next();
                Date fechaEmisionFirma = firmas.get(firmante);
                FirmaOrganismo firmaOrganismo = new FirmaOrganismo();
                firmaOrganismo.setOrganismoFirmante(firmante);
                firmaOrganismo.setFechaEmision(fechaEmisionFirma);
                diploma.getFirmas().add(firmaOrganismo);
            }
        }
        return this.diploma;
    }

    public void setId(Long id) {
        this.id = id;
        this.diploma.setId(id);
    }

    public Map<String, Date> getFirmas() {
        return this.firmas;
    }

    public void setFirmas(Map<String, Date> firmas) {
        this.firmas = firmas;
        if (this.diploma.getFirmas() == null) {
            this.diploma.setFirmas(new ArrayList<>());
        }
        Iterator<String> firmantesIterator = firmas.keySet().iterator();
        while (firmantesIterator.hasNext()) {
            String firmante = firmantesIterator.next();
            Date fechaEmisionFirma = firmas.get(firmante);
            FirmaOrganismo firmaOrganismo = new FirmaOrganismo();
            firmaOrganismo.setDiploma(this.diploma);
            firmaOrganismo.setOrganismoFirmante(firmante);
            firmaOrganismo.setFechaEmision(fechaEmisionFirma);
            diploma.getFirmas().add(firmaOrganismo);
        }
    }

    public Long getId() {
        return this.id;
    }


    public Long getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
        this.diploma.setIdcustomer(idCliente);
    }

    public String getNombreCompleto() {
        return this.nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
        this.diploma.setName(nombreCompleto);
    }

    public String getTitulacion() {
        return this.titulacion;
    }

    public void setTitulacion(String titulacion) {
        this.titulacion = titulacion;
        if (diploma.getTitulacion() == null) {
            Titulacion titulacion1 = new Titulacion();
            diploma.setTitulacion(titulacion1);
        }
        diploma.getTitulacion().setName(titulacion);
    }

    public String getRegionOComarca() {
        return this.regionOComarca;
    }

    public void setRegionOComarca(String regionOComarca) {
        this.regionOComarca = regionOComarca;
        this.diploma.setRegion(regionOComarca);
    }

    public String getContinente() {
        return this.continente;
    }

    public void setContinente(String continente) {
        this.continente = continente;
    }

}
