package com.mfc.infra.dto;

import java.io.Serializable;


public interface IArqDTO<ID, D> extends Serializable {


    ID getId();

    void setEntity(D entity);

    D getEntity();

    void actualizarEntidad(D entity);

}
