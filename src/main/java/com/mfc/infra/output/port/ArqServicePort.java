package com.mfc.infra.output.port;

import com.mfc.infra.dto.IArqDTO;

import java.util.List;


public interface ArqServicePort<T, D extends IArqDTO, ID> {

    D crear(D entity);

    D actualizar(D entity);

    int borrar(D entity);

    int borrar(List<D> entities);
    void borrar();

    D buscarPorId(ID id);

    List<D> buscarTodos();

    List<D> buscarCoincidenciasEstricto(D filterObject);

    List<D> buscarCoincidenciasNoEstricto(D filterObject);

    String getDocumentEntityClassname();

}
