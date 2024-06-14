package com.mfc.backend.microdiplomas.api.command.service;

import com.mfc.backend.microdiplomas.api.command.*;
import com.mfc.backend.microdiplomas.api.command.acciones.ActualizarDiplomaCommand;
import com.mfc.backend.microdiplomas.api.command.acciones.BorrarDiplomaCommand;
import com.mfc.backend.microdiplomas.api.command.acciones.BuscarDiplomasDeCliente;
import com.mfc.backend.microdiplomas.api.command.acciones.BuscarTodosLosDiplomasCommand;
import com.mfc.backend.microdiplomas.api.dto.DiplomaDTOArq;
import com.mfc.backend.microdiplomas.api.usecases.ActualizarDiplomaUseCase;
import com.mfc.backend.microdiplomas.api.usecases.BorrarTodosLosDiplomasUseCase;
import com.mfc.backend.microdiplomas.api.usecases.ConsultasDiplomasUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiplomaCommandService {

    @Autowired
    private CommandHandler commandHandler;

    @Autowired
    ActualizarDiplomaUseCase actualizarDiplomaUseCase;
    @Autowired
    BorrarTodosLosDiplomasUseCase borrarTodosLosDiplomasUseCase;
    @Autowired
    ConsultasDiplomasUseCase consultasDiplomasUseCase;

    public List<DiplomaDTOArq> getDiplomasDeLaRegionProvenza() {
        return null;
    }

    public DiplomaDTOArq actualizarDiploma(DiplomaDTOArq diplomaDTO) {
        ActualizarDiplomaCommand command = new ActualizarDiplomaCommand(actualizarDiplomaUseCase, diplomaDTO);
        commandHandler.registerCommand(ActualizarDiplomaCommand.class, command);
        return (DiplomaDTOArq) commandHandler.executeCommand(ActualizarDiplomaCommand.class);
    }

    public void borrarDiplomas() {
        BorrarDiplomaCommand command = new BorrarDiplomaCommand(borrarTodosLosDiplomasUseCase, new DiplomaDTOArq());
        commandHandler.registerCommand(BorrarDiplomaCommand.class, command);
        commandHandler.executeCommand(BorrarDiplomaCommand.class);
    }

    public List<DiplomaDTOArq> buscarDiplomasDeCustomer(Long id) {
        DiplomaDTOArq search = new DiplomaDTOArq();
        search.setId(id);
        BuscarDiplomasDeCliente command = new BuscarDiplomasDeCliente(consultasDiplomasUseCase, search);
        commandHandler.registerCommand(BuscarDiplomasDeCliente.class, command);
        return (List<DiplomaDTOArq>) commandHandler.executeCommand(BuscarDiplomasDeCliente.class);
    }

    public List<DiplomaDTOArq> buscarDiplomasPorNombreCustomer(String name) {
        DiplomaDTOArq search = new DiplomaDTOArq();
        search.setName(name);
        BuscarTodosLosDiplomasCommand command = new BuscarTodosLosDiplomasCommand(consultasDiplomasUseCase, search);
        commandHandler.registerCommand(BuscarTodosLosDiplomasCommand.class, command);
        return (List<DiplomaDTOArq>) commandHandler.executeCommand(BuscarTodosLosDiplomasCommand.class);
    }

    public List<DiplomaDTOArq> buscarTodosLosDiplomas() {
        BuscarTodosLosDiplomasCommand command = new BuscarTodosLosDiplomasCommand(consultasDiplomasUseCase,
                new DiplomaDTOArq());
        commandHandler.registerCommand(BuscarTodosLosDiplomasCommand.class, command);
        return (List<DiplomaDTOArq>) commandHandler.executeCommand(BuscarTodosLosDiplomasCommand.class);
    }


}
