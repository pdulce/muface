package com.mfc.microdiplomasWithMongo.api;

import com.mfc.infra.controller.ArqBaseRestController;
import com.mfc.microdiplomasWithMongo.api.dto.DiplomaDTO;
import com.mfc.microdiplomasWithMongo.api.usecases.DiplomaMUseCasesFacade;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "diplomasFacadeMongo")
public class DiplomaMongoAPI extends ArqBaseRestController {
    @Autowired
    DiplomaMUseCasesFacade diplomaUseCasesFacade;

    @GetMapping(value = "allDiplomas", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTO> getAllDiplomas() {
        return this.diplomaUseCasesFacade.consultarTodosLosDiplomas();
    }


    @GetMapping(value = "diplomasByCustomer", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTO> getAllDiplomasByCustomerID(@RequestParam Long idCliente) {
        return this.diplomaUseCasesFacade.consultaDiplomasDeCliente(idCliente);
    }

    @GetMapping(value = "diplomasByName", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTO> getAllDiplomasByCustomerName(@RequestParam String nombreDePila) {
        return this.diplomaUseCasesFacade.consultaDiplomasDeClientesConNombre(nombreDePila);
    }

    @PostMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public DiplomaDTO create(@RequestBody @NotNull DiplomaDTO diplomaDTO) {
        return this.diplomaUseCasesFacade.crearDiploma(diplomaDTO);
    }

    @PutMapping(produces=MediaType.APPLICATION_JSON_VALUE)
    public DiplomaDTO update(@RequestBody @NotNull DiplomaDTO diplomaDTO) {
        return this.diplomaUseCasesFacade.actualizarDiploma(diplomaDTO);
    }

    @DeleteMapping(value = "deleteAll", produces=MediaType.APPLICATION_JSON_VALUE)
    public void deleteAll() {
        this.diplomaUseCasesFacade.borrarTodosLosDiplomas();
    }

    /*** **/

    @GetMapping(value = "getDiplomasDeLaRegionProvenza", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<DiplomaDTO> getDiplomasDeLaRegionProvenza() {
        return this.diplomaUseCasesFacade.consultaDiplomasDeRegionProvenza();
    }


}
