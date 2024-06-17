package com.mfc.infra.command;

public interface IArqCommand<R, P> {
    R execute(P params);

}