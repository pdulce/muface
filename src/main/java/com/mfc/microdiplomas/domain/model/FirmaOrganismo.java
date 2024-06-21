package com.mfc.microdiplomas.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class FirmaOrganismo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "diploma_id")
    private Diploma diploma;

    @Column
    @NotEmpty
    private String organismoFirmante;

    @Column
    private Date fechaEmision;

}