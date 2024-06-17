package com.mfc.infra.service;

import com.mfc.infra.dto.IArqDTO;

import java.util.List;


public interface ArqServicePort<T, D extends IArqDTO, ID> {

    D crear(D entity);

    D actualizar(D entity);

    int borrarEntidades(D entity);

    int borrarEntidades(List<D> entities);

    void borrarTodos();

    D buscarPorId(ID id);

    List<D> buscarTodos();

    List<D> buscarCoincidenciasEstricto(D filterObject);

    List<D> buscarCoincidenciasNoEstricto(D filterObject);

}
