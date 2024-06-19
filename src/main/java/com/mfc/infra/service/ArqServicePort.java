package com.mfc.infra.service;

import com.mfc.infra.dto.IArqDTO;

import java.util.List;


public interface ArqServicePort<D extends IArqDTO, ID> {

    D crear(D entity);

    D actualizar(D entity) throws NoSuchMethodException;

    D buscarPorId(ID id);

    int borrarEntidad(ID id);
    int borrarEntidad(D entity);

    int borrarEntidades(List<D> entities);

    void borrarTodos();

    List<D> buscarTodos();

    List<D> buscarCoincidenciasEstricto(D filterObject);

    List<D> buscarCoincidenciasNoEstricto(D filterObject);

}
