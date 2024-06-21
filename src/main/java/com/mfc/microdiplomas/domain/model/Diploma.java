package com.mfc.microdiplomas.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Diploma {

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
    private Titulacion titulacion;

    @OneToMany(mappedBy = "diploma", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FirmaOrganismo> firmas;

}

