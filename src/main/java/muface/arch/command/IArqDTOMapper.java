package muface.arch.command;

public interface IArqDTOMapper<IArqEntidad, D extends IArqDTO> {
    D map(IArqEntidad entity);

    D newInstance();

}
