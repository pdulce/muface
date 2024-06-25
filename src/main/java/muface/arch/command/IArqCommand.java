package muface.arch.command;

public interface IArqCommand<R, P> {
    R executeInner(P params);

}