package muface.application.domain.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "diplomas")
public class DiplomaDocument implements Serializable {

    @Id
    private String id;

    private Long idcustomer;

    private String name;

    private String titulo;

    private String region;



}
