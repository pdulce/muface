package muface.arch.command;

public interface IArqCommand<R, P> {
    R execute(P params);

}