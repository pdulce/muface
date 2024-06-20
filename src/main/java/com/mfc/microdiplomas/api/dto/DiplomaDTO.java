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
    private Map<Long, String> titulacion;
    private Map<Long, Map<String, Date>> firmas;

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
        if (this.diploma.getTitulacion() != null) {
            Map<Long, String> tiulacionContent = new HashMap<>();
            tiulacionContent.put(this.diploma.getTitulacion().getId(), this.diploma.getTitulacion().getName());
            this.titulacion = tiulacionContent;
        }

        this.regionOComarca = this.diploma.getRegion();
        this.firmas = new HashMap<>();
        if (this.diploma.getFirmas() != null) {
            this.diploma.getFirmas().forEach((firma) -> {
                Map<String, Date> contenidoFirma = new HashMap<>();
                contenidoFirma.put(firma.getOrganismoFirmante(), firma.getFechaEmision());
                this.firmas.put(firma.getId(), contenidoFirma);
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
            Map<Long, String> tiulacionContent = new HashMap<>();
            tiulacionContent.put(this.diploma.getTitulacion().getId(), this.diploma.getTitulacion().getName());
            this.titulacion = tiulacionContent;
            this.diploma.getTitulacion().setDiploma(this.diploma);
        }

        if (this.firmas != null && !this.firmas.isEmpty())  {
            this.diploma.setFirmas(new ArrayList<>());
            Iterator<Long> firmantesIterator = this.firmas.keySet().iterator();
            while (firmantesIterator.hasNext()) {
                Long idFirmante = firmantesIterator.next();
                String firmante = firmas.get(idFirmante).entrySet().iterator().next().getKey();
                Date fechaEmisionFirma = firmas.get(idFirmante).entrySet().iterator().next().getValue();
                FirmaOrganismo firmaOrganismo = new FirmaOrganismo();
                firmaOrganismo.setDiploma(this.diploma);
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

    public Map<Long, Map<String, Date>> getFirmas() {
        return this.firmas;
    }

    public void setFirmas(Map<Long, Map<String, Date>> firmasInput) {
        this.firmas = firmasInput;
        if (this.diploma.getFirmas() == null) {
            this.diploma.setFirmas(new ArrayList<>());
        }
        Iterator<Long> firmantesIterator = firmasInput.keySet().iterator();
        while (firmantesIterator.hasNext()) {
            Long idFirmante = firmantesIterator.next();
            String firmante = firmasInput.get(idFirmante).entrySet().iterator().next().getKey();
            Date fechaEmisionFirma = firmasInput.get(idFirmante).entrySet().iterator().next().getValue();
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
        Map.Entry<Long, String> entry = titulacion.entrySet().iterator().hasNext() ? null
                : titulacion.entrySet().iterator().next();
        if (entry != null) {
            return entry.getValue();
        }
        return null;
    }

    public void setTitulacion(Map<Long, String> titulacion) {
        this.titulacion = titulacion;
        if (diploma.getTitulacion() == null) {
            Titulacion titulacion1 = new Titulacion();
            diploma.setTitulacion(titulacion1);
        }
        Map.Entry<Long, String> entry = titulacion.entrySet().iterator().hasNext() ? null
                : titulacion.entrySet().iterator().next();
        if (entry != null) {
            diploma.getTitulacion().setId(entry.getKey());
            diploma.getTitulacion().setName(entry.getValue());
        }
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
