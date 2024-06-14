package com.mfc.infra.output.port;

import java.util.List;


public interface ArqRelationalServicePort<T, IDTO, ID> {

    IDTO crear(IDTO entity);

    IDTO actualizar(IDTO entity);

    int borrar(IDTO entity);

    int borrar(List<IDTO> entities);
    void borrar();

    IDTO buscarPorId(ID id);

    List<IDTO> buscarTodos();

    List<IDTO> buscarPorCampoValor(String fieldName, Object fieldValue);

    String getDocumentEntityClassname();

}
