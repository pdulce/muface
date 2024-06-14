package com.mfc.infra.controller;

import com.mfc.infra.output.port.ArqEventStoreInputPort;
import com.mfc.infra.output.port.ArqSagaOrchestratorPort;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "auditorias")
public class ArqAuditoriasControllerAPI {

    protected static final String TYPE_OF_STORE_AUDIT = "audit";
    protected static final String TYPE_OF_STORE_TRANSAC_DISTRIB = "transac-distrib";
    Logger logger = LoggerFactory.getLogger(ArqAuditoriasControllerAPI.class);
    @Autowired
    protected MessageSource messageSource;

    @Autowired(required=false)
    protected ArqSagaOrchestratorPort orchestratorManager;
    @Autowired(required=false)
    protected ArqEventStoreInputPort eventStoreConsumerAdapter;

    /** ENDPOINTS QUE DAN ACCESO A LA INFORMACIÓN DE CUALQUIER AUDITORIA DE CUALQUIER APLICACION **/

    @GetMapping(value = "{applicationId}", produces= MediaType.APPLICATION_JSON_VALUE)
    public List<Object> getAllFromEventStoreCustomers(@PathVariable @NotEmpty String applicationId) {
        return this.eventStoreConsumerAdapter.findAllByApp(TYPE_OF_STORE_AUDIT, applicationId);
    }

    @GetMapping(value = "{applicationId}/{almacen}", produces= MediaType.APPLICATION_JSON_VALUE)
    public List<Object> getAllFromEventStoreCustomers(@PathVariable @NotEmpty String applicationId,
                                                                   @PathVariable @NotEmpty String almacen) {
        return this.eventStoreConsumerAdapter.findAllByAppAndStore(TYPE_OF_STORE_AUDIT, applicationId, almacen);
    }

    @GetMapping(value = "{applicationId}/{almacen}/{idAgregado}",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Object getAllEventsFromIdFromRedis(@PathVariable @NotEmpty String applicationId,
                                                    @PathVariable @NotEmpty String almacen,
                                                    @PathVariable @NotEmpty String idAgregado) {
        return this.eventStoreConsumerAdapter.findAggregateByAppAndStoreAndAggregateId(TYPE_OF_STORE_AUDIT,
                applicationId, almacen, idAgregado);
    }

    /** ENDPOINTS QUE DAN ACCESO A LA INFORMACIÓN DE CUALQUIER TRANSACCION EN CUALQUIER APLICACION **/

    @GetMapping(value = "transacciones-distribuidas/{applicationId}", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Object> getAllTransactionsOfApplication(@PathVariable @NotEmpty String applicationId,
                                                                     @PathVariable @NotEmpty String saga) {
        return this.eventStoreConsumerAdapter.findAllByApp(TYPE_OF_STORE_TRANSAC_DISTRIB, applicationId);
    }

    @GetMapping(value = "transacciones-distribuidas/{applicationId}/{saga}", produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Object> getAllTransactionsOfSagaInApp(@PathVariable @NotEmpty String applicationId,
                                                                   @PathVariable @NotEmpty String saga) {
        return this.eventStoreConsumerAdapter.findAllByAppAndStore(TYPE_OF_STORE_TRANSAC_DISTRIB, applicationId, saga);
    }

    @GetMapping(value = "transacciones-distribuidas/{applicationId}/{saga}/{transactionId}",
            produces=MediaType.APPLICATION_JSON_VALUE)
    public Object getAllStepsInSagaTransactionId(@PathVariable @NotEmpty String applicationId,
                                                       @PathVariable @NotEmpty String saga,
                                                       @PathVariable @NotEmpty String transactionId) {
        return this.eventStoreConsumerAdapter.findAggregateByAppAndStoreAndAggregateId(TYPE_OF_STORE_TRANSAC_DISTRIB,
                applicationId, saga, transactionId);
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
