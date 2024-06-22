package com.mfc.infra.event.futuro.saga;

import com.mfc.infra.configuration.ArqConfigProperties;
import com.mfc.infra.dto.IArqDTO;
import com.mfc.infra.event.ArqEvent;
import com.mfc.infra.event.futuro.publishers.ArqCommandEventPublisherPort;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Transactional
public abstract class ArqStepArqSagaManagerAdapter<D extends IArqDTO, ID> implements ArqSagaStepPort<Object, ID> {

    protected Logger logger = LoggerFactory.getLogger(ArqStepArqSagaManagerAdapter.class);
    @Autowired
    ArqConfigProperties arqConfigProperties;
    @Autowired(required = false)
    protected ArqCommandEventPublisherPort arqCommandEventPublisherPort;

    /** metodos para conectar con transacciones distribuidas bajo el patrón SAGA **/

    /****
     Las clases Service que extienda de CommandStepSagaAdapter deberán implementar el método listen, cada una con un
     group-id diferente:

     protected static final String GROUP_ID = "saga-step-group-<texto-libre>-<secuencial>";

        @KafkaListener(topics = <topic_Step> groupId = <SU_GROUP_ID>)
        public void listen(Event event) {
            super.procesarEvento(event);
        }
     ***/

    public void processStepEvent(ArqEvent event) {
        if (!arqConfigProperties.isEventBrokerActive()) {
            logger.error("Debe tener activa la configuración de uso de mensajería en la arquitectura");
            return;
        }
        event.getSagaStepInfo().setStepNumber(getOrderStepInSaga());
        if (event.getSagaStepInfo().isDoCompensateOp()) {
            orderSagaCompensation(event);
            this.arqCommandEventPublisherPort.publish(ArqSagaOrchestratorPort.SAGA_FROM_STEP_TOPIC, event);
            logger.info("Se ha informado al orchestrator que la operación de compensación en el step "
                    + event.getSagaStepInfo().getStepNumber()
                    + " para la transacción núm: " + event.getSagaStepInfo().getTransactionIdentifier()
                    + (event.getSagaStepInfo().getStateOfOperation() == ArqEvent.SAGA_OPE_FAILED
                    ? " ha fallado" : " se ha realizado de forma satisfactoria"));
        } else {
            event.getSagaStepInfo().setLastStep(isLastStepInSaga());
            orderSagaOperation(event);
            this.arqCommandEventPublisherPort.publish(ArqSagaOrchestratorPort.SAGA_FROM_STEP_TOPIC, event);
            logger.info("Se ha informado al orchestrator que la operación de consolidación en el step "
                    + event.getSagaStepInfo().getNextStepNumberToProccess()
                    + " para la transacción núm: " + event.getSagaStepInfo().getTransactionIdentifier()
                    + (event.getSagaStepInfo().getStateOfOperation() == ArqEvent.SAGA_OPE_FAILED
                    ? " ha fallado" : " se ha realizado de forma satisfactoria"));
        }

    }
    private void orderSagaOperation(ArqEvent event) {
        //invocamos a la implementación específica del service del microservicio
        Object newdata = getNewData(getWrapper(event));
        try{
            newdata = this.doSagaOperation(event);
            event.setId(ArqEvent.STEP_ID_PREFIX + getOrderStepInSaga());
            event.getSagaStepInfo().setStateOfOperation(ArqEvent.SAGA_OPE_SUCCESS);
        } catch (ConstraintViolationException exc) {
            event.getSagaStepInfo().setStateOfOperation(ArqEvent.SAGA_OPE_FAILED);
            String msg = "doSagaOperation failed: Cause " + exc.getLocalizedMessage();
            event.getSagaStepInfo().setErrorMsgOperation(msg);
            logger.error(msg);
        } catch (Throwable exc) {
            event.getSagaStepInfo().setStateOfOperation(ArqEvent.SAGA_OPE_FAILED);
            String msg = "doSagaOperation failed: Cause " + exc.getLocalizedMessage();
            event.getSagaStepInfo().setErrorMsgOperation(msg);
            logger.error(msg);
        } finally {
            event.getInnerEvent().setNewData(newdata);
        }
    }

    private void orderSagaCompensation(ArqEvent event) {
        //invocamos a la implementación específica del service del microservicio
        Object newdata = getNewData(getWrapper(event));
        try {
            newdata = this.doSagaCompensation(event);
            event.getSagaStepInfo().setStateOfCompensation(ArqEvent.SAGA_OPE_SUCCESS);
        } catch (Throwable exc) {
            event.getSagaStepInfo().setStateOfCompensation(ArqEvent.SAGA_OPE_FAILED);
            String msg = "doSagaOperation failed: Cause " + exc.getLocalizedMessage();
            event.getSagaStepInfo().setErrorMsgCompensation(msg);
            logger.error(msg);
        } finally {
            event.getInnerEvent().setNewData(newdata);
        }
    }

    @Override
    public abstract Object doSagaOperation(ArqEvent event) throws Throwable;

    @Override
    public abstract Object doSagaCompensation(ArqEvent event) throws Throwable;

    @Override
    public abstract String getSagaName();

    @Override
    public abstract int getOrderStepInSaga();

    @Override
    public abstract boolean isLastStepInSaga();

    @Override
    public abstract String getTypeOrOperation();

    protected abstract Object getNewData(Object dataReceivedFromPreviousStep);

    protected abstract Object getWrapper(ArqEvent event);


}
