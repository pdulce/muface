package muface.arch.event.futuro.consumers;

import muface.arch.event.ArqEvent;

public interface ArqCommandEventListenerPort {

    void listen(ArqEvent<?> eventArch);
    void procesarEvento(ArqEvent<?> eventArch);
}
