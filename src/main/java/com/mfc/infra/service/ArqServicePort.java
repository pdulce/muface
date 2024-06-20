package com.mfc.infra.service;

import com.mfc.infra.dto.IArqDTO;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ArqServicePort<D extends IArqDTO, ID> {

    D crear(D entity);

    D actualizar(D entity) throws NoSuchMethodException;

    D buscarPorId(ID id);

    String borrarEntidad(ID id);
    String borrarEntidad(D entity);

    String borrarEntidades(List<D> entities);

    String borrarTodos();

    List<D> buscarTodos();

    List<D> buscarCoincidenciasEstricto(D filterObject);

    List<D> buscarCoincidenciasNoEstricto(D filterObject);

    Page<D> buscarTodosPaginados(int page, int size);

    Page<D> buscarCoincidenciasEstrictoPaginados(D filterObject, int page, int size);

    Page<D> buscarCoincidenciasNoEstrictoPaginados(D filterObject, int page, int size);

}
