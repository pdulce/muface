package muface.arch.command;

public interface IArqDTOMapper<Serializable, D extends IArqDTO> {
    D map(Serializable entity);

    D newInstance();

}
