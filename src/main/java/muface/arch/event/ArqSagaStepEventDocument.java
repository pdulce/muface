package muface.arch.event;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sagas")
@Data
public class ArqSagaStepEventDocument {

    @Id
    private String id;

    private String applicationId;
    private String saga;
    private String stepId;
    private String idEntry;
    private ArqEvent<?> event;
}
