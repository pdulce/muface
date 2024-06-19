package com.mfc.microdiplomas.domain.model;

import com.mfc.infra.utils.ArqConstantMessages;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Data
public class Diploma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    //@NotNull(message = "{" + ArqConstantMessages.NOT_EMPTY_LET + "}")
    @Max(value = 999999, message = "{idCliente.max}")
    private Long idcustomer;

    @Column
    @NotEmpty(message = "{nombreCliente.notnull}")
    private String name;

    @Column
    private String titulo;

    @Column
    private String region;


}
