package com.mfc.microdiplomas.api;

import com.mfc.microdiplomas.api.dto.DiplomaDTO;
import com.mfc.microdiplomas.api.facade.DiplomaFacade;
import com.mfc.infra.controller.ArqBaseRestController;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "diplomasFacade")
public class DiplomaWithFacadeAPI extends ArqBaseRestController {
    @Autowired
    DiplomaFacade diplomaFacade;

    @GetMapping(value = "allDiplomas", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTO> getAllDiplomas() {

        return this.diplomaFacade.consultarTodosLosDiplomas();
    }


    @GetMapping(value = "allDiplomasByCustomerName", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTO> getAllDiplomasByCustomerName(@RequestParam String name) {
        return this.diplomaFacade.consultaDiplomasDeClientesConNombre(name);
    }

    @GetMapping(value = "allDiplomasByCustomerID", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTO> getAllDiplomasByCustomerID(@RequestParam Long customerid) {
        return this.diplomaFacade.consultaDiplomasDeCliente(customerid);
    }

    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public DiplomaDTO create(@RequestBody @NotNull DiplomaDTO diplomaDTO) {
        return this.diplomaFacade.crearDiploma(diplomaDTO);
    }

    @PutMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public DiplomaDTO update(@RequestBody @NotNull DiplomaDTO diplomaDTO) {
        return this.diplomaFacade.actualizarDiploma(diplomaDTO);
    }

    @DeleteMapping(value = "deleteAll", produces=MediaType.APPLICATION_JSON_VALUE)
    public void deleteAll() {
        this.diplomaFacade.borrarTodosLosDiplomas();
    }

    /*** **/

    @GetMapping(value = "getDiplomasDeLaRegionProvenza", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTO> getDiplomasDeLaRegionProvenza() {
        return this.diplomaFacade.consultaDiplomasDeRegionProvenza();
    }


}
