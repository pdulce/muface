package muface.arch.controller;

import muface.arch.event.ArqEvent;
import muface.arch.event.futuro.output.ArqEventStoreInputPort;
import muface.arch.event.futuro.output.ArqSagaStepInputPort;
import muface.arch.event.futuro.saga.ArqSagaOrchestratorPort;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "auditorias")
public class ArqAuditoriasControllerAPI {
    Logger logger = LoggerFactory.getLogger(ArqAuditoriasControllerAPI.class);
    @Autowired
    protected MessageSource messageSource;

    @Autowired(required=false)
    protected ArqSagaOrchestratorPort orchestratorManager;
    @Autowired(required=false)
    protected ArqEventStoreInputPort auditoriasAdapter;

    @Autowired(required=false)
    protected ArqSagaStepInputPort sagaAdapter;

    /** ENDPOINTS QUE DAN ACCESO A LA INFORMACIÓN DE CUALQUIER AUDITORIA DE CUALQUIER APLICACION **/

    @GetMapping
    public List<ArqEvent<?>> getAllAuditorias() {
        return this.auditoriasAdapter.findAll();
    }
    @GetMapping(value = "{applicationId}", produces= MediaType.APPLICATION_JSON_VALUE)
    public List<ArqEvent<?>> getAllAuditoriasByApp(@PathVariable @NotEmpty String applicationId) {
        return this.auditoriasAdapter.findAllByApp(applicationId);
    }

    @GetMapping(value = "{applicationId}/{almacen}", produces= MediaType.APPLICATION_JSON_VALUE)
    public List<ArqEvent<?>> getAllAuditoriasByAppAndStore(@PathVariable @NotEmpty String applicationId,
                                                           @PathVariable @NotEmpty String almacen) {
        return this.auditoriasAdapter.findAllByAppAndStore(applicationId, almacen);
    }

    @GetMapping(value = "{applicationId}/{almacen}/{idAgregado}",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public List<ArqEvent<?>> getAllAuditoriasByAppAndStoreAndIdAgregado(@PathVariable @NotEmpty String applicationId,
                                                    @PathVariable @NotEmpty String almacen,
                                                    @PathVariable @NotEmpty String idAgregado) {
        return this.auditoriasAdapter.findAggregateByAppAndStoreAndAggregateId(applicationId, almacen, idAgregado);
    }

    /** ENDPOINTS QUE DAN ACCESO A LA INFORMACIÓN DE CUALQUIER TRANSACCION EN CUALQUIER APLICACION **/

    @GetMapping(value = "transacciones-distribuidas/{applicationId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<ArqEvent<?>> getAllTransactionsOfApplication(@PathVariable @NotEmpty String applicationId,
                                                                     @PathVariable @NotEmpty String saga) {
        return this.sagaAdapter.findAllByApp(applicationId);
    }

    @GetMapping(value = "transacciones-distribuidas/{applicationId}/{saga}", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<ArqEvent<?>> getAllTransactionsOfSagaInApp(@PathVariable @NotEmpty String applicationId,
                                                                   @PathVariable @NotEmpty String saga) {
        return this.sagaAdapter.findAllByAppAndSaga(applicationId, saga);
    }

    @GetMapping(value = "transacciones-distribuidas/{applicationId}/{saga}/{transactionId}",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public List<ArqEvent<?>> getAllStepsInSagaTransactionId(@PathVariable @NotEmpty String applicationId,
                                                       @PathVariable @NotEmpty String saga,
                                                       @PathVariable @NotEmpty String transactionId) {
        return this.sagaAdapter.findAggregateByAppAndSagaAndAggregateId(applicationId, saga, transactionId);
    }


    @GetMapping(value = "transacciones-distribuidas/{applicationId}/{saga}/{transactionId}/status")
    private String getSagaEstadoFinalizacion(@PathVariable @NotEmpty String applicationId,
                                             @PathVariable @NotEmpty String saga,
                                             @PathVariable @NotEmpty String transactionId) {
        logger.info("...Comprobamos si finalizó la transacción number: " + transactionId + " de la saga " + saga);
        String[] messageKeyAndArgs = this.orchestratorManager.
                getLastStateOfTansactionInSaga(applicationId, saga, transactionId);
        Object[] args = new Object[messageKeyAndArgs.length - 1];
        for (int i = 1; i < messageKeyAndArgs.length; i++) {
            args[i-1] = messageKeyAndArgs[i];
        }
        return messageSource.getMessage(messageKeyAndArgs[0], args, new Locale("es"));
    }

}
