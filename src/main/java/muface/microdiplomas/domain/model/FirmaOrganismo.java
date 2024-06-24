package muface.microdiplomas.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class FirmaOrganismo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "diploma_id")
    @JsonBackReference
    private Diploma diploma;

    @Column
    @NotEmpty
    private String organismoFirmante;

    @Column
    private Date fechaEmision;

}
