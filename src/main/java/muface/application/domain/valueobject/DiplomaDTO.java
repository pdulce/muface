package muface.application.domain.valueobject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import muface.arch.command.IArqDTO;
import muface.application.domain.model.Diploma;
import muface.application.domain.model.FirmaOrganismo;
import muface.application.domain.model.Titulacion;
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

    public void setTitulacionDeno(String titulacionDeno) {
        this.titulacion = new HashMap<>();
        this.titulacion.put(Long.valueOf("0"), titulacionDeno);
    }

    public String getTitulacionDeno() {
        return this.titulacion == null || this.titulacion.isEmpty() ? "" : this.titulacion.values().iterator().next();
    }


    public String getInnerOrderField(String fieldInDto) {
        if (fieldInDto.contentEquals("id")) {
            return "id";
        } else if (fieldInDto.contentEquals("idCliente")) {
            return "idcustomer";
        } else if (fieldInDto.contentEquals("nombreCompleto")) {
            return "name";
        } else if (fieldInDto.contentEquals("regionOComarca")) {
            return "region";
        } else {
            return "";
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
    public void actualizarEntidad(Diploma entidadBBDD) {
        entidadBBDD.setIdcustomer(this.idCliente);
        entidadBBDD.setName(this.nombreCompleto);
        entidadBBDD.setRegion(this.regionOComarca);

        // Actualizar la Titulacion
        if (this.titulacion != null && !this.titulacion.isEmpty()) {
            Map.Entry<Long, String> entry = titulacion.entrySet().iterator().next();
            Long idTitulacion = entry.getKey();
            String nombreTitulacion = entry.getValue();
            if (entidadBBDD.getTitulacion() == null) {
                Titulacion nuevaTitulacion = new Titulacion();
                nuevaTitulacion.setName(nombreTitulacion);
                nuevaTitulacion.setDiploma(entidadBBDD);
                entidadBBDD.setTitulacion(nuevaTitulacion);
            } else if (idTitulacion.longValue() == entidadBBDD.getTitulacion().getId().longValue()){
                entidadBBDD.getTitulacion().setName(nombreTitulacion);
            }
        } else if (entidadBBDD.getTitulacion() != null) {
            entidadBBDD.setTitulacion(null); // O manejo adecuado de eliminación si se desea
        }

        // Actualizar las Firmas
        if (this.firmas != null && !this.firmas.isEmpty()) {
            List<FirmaOrganismo> nuevasFirmas = new ArrayList<>();

            for (Map.Entry<Long, Map<String, Date>> firmaEntry : this.firmas.entrySet()) {
                Long idFirmante = firmaEntry.getKey();
                Map<String, Date> datosFirma = firmaEntry.getValue();
                Map.Entry<String, Date> entry = datosFirma.entrySet().iterator().next();
                String firmante = entry.getKey();
                Date fechaEmisionFirma = entry.getValue();

                FirmaOrganismo firmaOrganismo = entidadBBDD.getFirmas().stream()
                        .filter(f -> f.getId().equals(idFirmante))
                        .findFirst()
                        .orElse(new FirmaOrganismo());
                if (firmaOrganismo.getId() == null) {
                    firmaOrganismo.setId(idFirmante);
                    firmaOrganismo.setDiploma(entidadBBDD);
                    nuevasFirmas.add(firmaOrganismo);
                } else {
                    firmaOrganismo.setOrganismoFirmante(firmante);
                    firmaOrganismo.setFechaEmision(fechaEmisionFirma);
                }
            }
            if (!nuevasFirmas.isEmpty()) {
                entidadBBDD.getFirmas().clear();
                entidadBBDD.getFirmas().addAll(nuevasFirmas);
            }
        } else if (entidadBBDD.getFirmas() != null) {
            entidadBBDD.getFirmas().clear(); // O manejo adecuado de eliminación si se desea
        }
    }

    @Override
    public Long getId() {
        return this.id;
    }

}
