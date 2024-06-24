package muface.arch.event.futuro.publishers;

import muface.arch.event.ArqEvent;

public interface ArqCommandEventPublisherPort {
    void publish(String topic, ArqEvent<?> eventArch);
}
