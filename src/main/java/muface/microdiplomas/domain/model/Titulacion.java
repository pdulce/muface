package muface.microdiplomas.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
public class Titulacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "diploma_id")
    @JsonBackReference
    private Diploma diploma;

    @Column
    @NotEmpty(message = "El nombre de la titulacion no puede estar vacío")
    private String name;

}
