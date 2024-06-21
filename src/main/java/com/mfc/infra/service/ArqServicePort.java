package com.mfc.infra.service;

import com.mfc.infra.dto.IArqDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface ArqServicePort<D extends IArqDTO, ID> {

    String getRepositoryEntityOfDTO();
    D crear(D entity);

    D actualizar(D entity) throws NoSuchMethodException;

    D buscarPorId(ID id);

    List<D> buscarPorIds(List<ID> ids);

    String borrarEntidad(ID id);
    String borrarEntidad(D entity);

    String borrarEntidades(List<D> entities);

    String borrarTodos();

    List<D> buscarTodos();

    List<D> buscarTodosConOrdenacion(Sort sort);

    List<D> buscarCoincidenciasEstricto(D filterObject);

    List<D> buscarCoincidenciasNoEstricto(D filterObject);

    Page<D> buscarTodosPaginados(Pageable pageable);

    Page<D> buscarCoincidenciasEstrictoPaginados(D filterObject, Pageable pageable);

    Page<D> buscarCoincidenciasNoEstrictoPaginados(D filterObject, Pageable pageable);

}
