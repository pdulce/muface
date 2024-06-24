package muface.arch.command;

import java.io.Serializable;


public interface IArqDTO<ID, D> extends Serializable {


    ID getId();

    String getInnerOrderField(String fieldInDto);

    void setEntity(D entity);

    D getEntity();

    void actualizarEntidad(D entity);

}
