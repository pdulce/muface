package com.mfc.infra.output.port;

import com.mfc.infra.dto.IArqDTO;

import java.util.List;


public interface ArqRelationalServicePort<T, IDTO extends IArqDTO, ID> {

    IDTO crear(IDTO entity);

    IDTO actualizar(IDTO entity);

    int borrar(IDTO entity);

    int borrar(List<IDTO> entities);
    void borrar();

    IDTO buscarPorId(ID id);

    List<IDTO> buscarTodos();

    List<IDTO> buscarCoincidenciasEstricto(IDTO filterObject);

    List<IDTO> buscarCoincidenciasNoEstricto(IDTO filterObject);

    String getDocumentEntityClassname();

}
