package com.mfc.microdiplomas.domain.service;

import com.mfc.infra.repository.ArqPortRepository;
import com.mfc.infra.service.ArqGenericService;
import com.mfc.infra.utils.ArqConversionUtils;
import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.domain.model.Diploma;
import com.mfc.microdiplomas.domain.repository.DiplomaJPARepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DiplomaDTOService extends ArqGenericService<DiplomaDTO, Long> {

    @Override
    public String getRepositoryEntityOfDTO() {
        return Diploma.class.getName();
    }

    public DiplomaDTOService(Map<String, ArqPortRepository<?, Long>> repositories) {
        super(repositories);
    }

    /*** métodos personalizados ***/

    public List<DiplomaDTO> buscarDiplomasPorNombreDeTitulacion(String nameOfTitulacion) {
        List<DiplomaDTO> resultado = new ArrayList<>();
        DiplomaJPARepository diplomaJPARepository = applicationContext.getBean(DiplomaJPARepository.class);
        List<Diploma> listaEntities = diplomaJPARepository.findDiplomasByTitulacionName(nameOfTitulacion);
        listaEntities.forEach((diploma) -> {
            DiplomaDTO diplomaDTO = new DiplomaDTO();
            diplomaDTO.setEntity(diploma);
            resultado.add(diplomaDTO);
        });
        return resultado;
    }

    public Page<DiplomaDTO> buscarDiplomasPorNombreDeTitulacion(String nameOfTitulacion, Pageable pageable) {
        DiplomaJPARepository diplomaJPARepository = applicationContext.getBean(DiplomaJPARepository.class);
        Pageable newPageable = ArqConversionUtils.changePageableOrderFields(new DiplomaDTO(), pageable);
        Page<Diploma> resultado = diplomaJPARepository.findDiplomasByTitulacionName(nameOfTitulacion, newPageable);
        return convertirAPageOfDtos(resultado, newPageable);
    }


}
