package com.mfc.backend.microdiplomas.api;

import com.mfc.backend.microdiplomas.api.command.service.DiplomaCommandService;
import com.mfc.backend.microdiplomas.api.dto.DiplomaDTOArq;
import com.mfc.infra.controller.ArqBaseRestController;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "diplomasCommand")
public class DiplomaWithCommandAPI extends ArqBaseRestController {
    @Autowired
    DiplomaCommandService diplomaCommandService;

    @GetMapping(value = "allDiplomas", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTOArq> getAllDiplomas() {

        return this.diplomaCommandService.buscarTodosLosDiplomas();
    }


    @GetMapping(value = "allDiplomasByCustomerName", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTOArq> getAllDiplomasByCustomerName(@RequestParam String name) {
        return this.diplomaCommandService.buscarDiplomasPorNombreCustomer(name);
    }

    @GetMapping(value = "allDiplomasByCustomerID", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTOArq> getAllDiplomasByCustomerID(@RequestParam Long customerid) {
        return this.diplomaCommandService.buscarDiplomasDeCustomer(customerid);
    }

    @PutMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public DiplomaDTOArq update(@RequestBody @NotNull DiplomaDTOArq diplomaDTO) {
        return this.diplomaCommandService.actualizarDiploma(diplomaDTO);
    }

    @DeleteMapping(value = "deleteAll", produces=MediaType.APPLICATION_JSON_VALUE)
    public void deleteAll() {
        this.diplomaCommandService.borrarDiplomas();
    }

    /*** **/

    @GetMapping(value = "getDiplomasDeLaRegionProvenza", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTOArq> getDiplomasDeLaRegionProvenza() {
        return this.diplomaCommandService.getDiplomasDeLaRegionProvenza();
    }


}
