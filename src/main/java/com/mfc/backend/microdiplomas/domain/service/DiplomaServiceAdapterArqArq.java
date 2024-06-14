package com.mfc.backend.microdiplomas.domain.service;

import com.mfc.backend.microdiplomas.api.dto.DiplomaDTOArq;
import com.mfc.backend.microdiplomas.domain.model.Diploma;
import com.mfc.backend.microdiplomas.domain.repository.DiplomaCommandRepository;
import com.mfc.infra.output.adapter.ArqArqRelationalServiceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiplomaServiceAdapterArqArq extends ArqArqRelationalServiceAdapter<Diploma, DiplomaDTOArq, Long> implements DiplomaServicePortArq {

    @Autowired
    private DiplomaCommandRepository repository;

    protected JpaRepository<Diploma, Long> getRepository() {
        return this.repository;
    }

    @Override
    public List<DiplomaDTOArq> getDiplomasDeLaRegionProvenza() {
        List<DiplomaDTOArq> diplomaDTOS = new ArrayList<>();
        Diploma dFilter = new Diploma();
        dFilter.setRegion("Provenza");
        Example<Diploma> example = Example.of(dFilter);
        repository.findAll(example).forEach((diploma -> {
            diplomaDTOS.add(new DiplomaDTOArq(diploma.getId(), diploma.getIdcustomer(), diploma.getName(), diploma.getTitulo(),
                    diploma.getRegion()));
        }));
        return diplomaDTOS;
    }

}
