package muface.microdiplomas.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import muface.arch.command.IArqEntidad;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Diploma implements Serializable, IArqEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Max(value = 999999, message = "{idCliente.max}")
    private Long idcustomer;

    @Column
    @NotEmpty(message = "{nombreCliente.notnull}")
    private String name;

    @Column
    private String region;

    @OneToOne(mappedBy = "diploma", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Titulacion titulacion;

    @OneToMany(mappedBy = "diploma", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FirmaOrganismo> firmas;

}

