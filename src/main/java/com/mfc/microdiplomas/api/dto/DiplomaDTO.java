package com.mfc.microdiplomas.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.domain.model.FirmaOrganismo;
import com.mfc.microdiplomas.domain.model.Titulacion;
import lombok.Data;

import java.util.*;

@Data
public class DiplomaDTO implements IArqDTO<Long, Diploma> {

    private Long id;
    private Long idCliente;
    private String nombreCompleto;
    private String regionOComarca;
    private Map<Long, String> titulacion;
    private Map<Long, Map<String, Date>> firmas;

    /** campo calculado transient que no está en el modelo (entidad-relacional o document-non-relational) **/
    private String continente;

    @Override
    public void setEntity(Diploma diploma) {
        this.id = diploma.getId();
        this.idCliente = diploma.getIdcustomer();
        this.nombreCompleto = diploma.getName();
        if (diploma.getTitulacion() != null) {
            Map<Long, String> tiulacionContent = new HashMap<>();
            tiulacionContent.put(diploma.getTitulacion().getId(), diploma.getTitulacion().getName());
            this.titulacion = tiulacionContent;
        }
        this.regionOComarca = diploma.getRegion();
        this.firmas = new HashMap<>();
        if (diploma.getFirmas() != null) {
            diploma.getFirmas().forEach((firma) -> {
                Map<String, Date> contenidoFirma = new HashMap<>();
                contenidoFirma.put(firma.getOrganismoFirmante(), firma.getFechaEmision());
                this.firmas.put(firma.getId(), contenidoFirma);
            });
        }
    }
    @Override
    @JsonIgnore
    public Diploma getEntity() {
        Diploma diploma = new Diploma();
        diploma.setId(this.id);
        diploma.setIdcustomer(this.idCliente);
        diploma.setName(this.nombreCompleto);
        diploma.setRegion(this.regionOComarca);

        if (this.titulacion != null) {
            Map.Entry<Long, String> entry = titulacion.entrySet().iterator().hasNext()
                    ? titulacion.entrySet().iterator().next() : null;
            if (entry != null) {
                diploma.setTitulacion(new Titulacion());
                diploma.getTitulacion().setName(entry.getValue());
                diploma.getTitulacion().setDiploma(diploma);
            }
        }

        if (this.firmas != null && !this.firmas.isEmpty())  {
            diploma.setFirmas(new ArrayList<>());
            Iterator<Long> firmantesIterator = this.firmas.keySet().iterator();
            while (firmantesIterator.hasNext()) {
                Long idFirmante = firmantesIterator.next();
                String firmante = firmas.get(idFirmante).entrySet().iterator().next().getKey();
                Date fechaEmisionFirma = firmas.get(idFirmante).entrySet().iterator().next().getValue();
                FirmaOrganismo firmaOrganismo = new FirmaOrganismo();
                firmaOrganismo.setOrganismoFirmante(firmante);
                firmaOrganismo.setFechaEmision(fechaEmisionFirma);
                firmaOrganismo.setDiploma(diploma);
                diploma.getFirmas().add(firmaOrganismo);
            }
        }
        return diploma;
    }

    @Override
    public void actualizarEntidad(Diploma entidadBBDD) {
        if (entidadBBDD == null) {
            return;
        }

        entidadBBDD.setIdcustomer(this.idCliente);
        entidadBBDD.setName(this.nombreCompleto);
        entidadBBDD.setRegion(this.regionOComarca);

        // Actualizar Titulacion
        if (this.titulacion != null && !this.titulacion.isEmpty()) {
            Map.Entry<Long, String> entry = this.titulacion.entrySet().iterator().next();
            if (entidadBBDD.getTitulacion() == null) {
                Titulacion titulacion = new Titulacion();
                titulacion.setDiploma(entidadBBDD);
                entidadBBDD.setTitulacion(titulacion);
            }
            entidadBBDD.getTitulacion().setName(entry.getValue());
        } else {
            entidadBBDD.setTitulacion(null);
        }

        // Actualizar Firmas
        if (this.firmas != null) {
            List<FirmaOrganismo> firmasNuevas = new ArrayList<>();
            this.firmas.forEach((idFirmante, contenidoFirma) -> {
                FirmaOrganismo firmaOrganismo = new FirmaOrganismo();
                firmaOrganismo.setId(idFirmante);
                firmaOrganismo.setOrganismoFirmante(contenidoFirma.keySet().iterator().next());
                firmaOrganismo.setFechaEmision(contenidoFirma.values().iterator().next());
                firmaOrganismo.setDiploma(entidadBBDD);
                firmasNuevas.add(firmaOrganismo);
            });
            entidadBBDD.getFirmas().clear();
            entidadBBDD.getFirmas().addAll(firmasNuevas);
        } else {
            entidadBBDD.getFirmas().clear();
        }
    }


    public Long getId() {
        return this.id;
    }

}
