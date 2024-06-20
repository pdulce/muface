package com.mfc.microdiplomas.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
public class Titulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "diploma_id")
    private Diploma diploma;

    @Column
    @NotEmpty(message = "{nombreCliente.notnull}")
    private String name;

}
