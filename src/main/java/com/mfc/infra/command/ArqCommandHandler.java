package com.mfc.infra.command;

import org.springframework.stereotype.Component;

@Component
public class ArqCommandHandler<R, P> {

    public R executeCommand(IArqCommand<R, P> command, P params) {
        return command.execute(params);
    }
}
