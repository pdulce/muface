package muface.arch.command;

import muface.arch.exceptions.ArqBussinessRuleException;
import muface.arch.exceptions.NotExistException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ArqUseCaseExecutor {

    @Autowired
    ApplicationContext applicationContext;

    @Transactional
    public <R, P> R executeUseCase(String useCaseName, P paramObj) {
        try {
            String usecaseCamelNotacion = useCaseName.substring(0,1).toLowerCase().concat(useCaseName.substring(1));
            IArqCommand<R, P> useCase = (IArqCommand<R, P>) applicationContext.getBean(usecaseCamelNotacion);
            if (useCase == null) {
                throw new RuntimeException("El caso de Uso <" + useCaseName + "> no existe");
            }
            return useCase.executeInner(paramObj);

        } catch (ConstraintViolationException | NotExistException | ArqBussinessRuleException excConstraint) {

            throw excConstraint;

        } catch (Throwable exc) {

            throw new ArqBussinessRuleException(exc.getMessage(), null);
        }
    }

    public <R extends Page, P> R executePaginationUseCase(String useCaseName, P paramObj, Pageable pageable) {
        try {
            String usecaseCamelNotacion = useCaseName.substring(0,1).toLowerCase().concat(useCaseName.substring(1));
            IArqCommandPagination<R, P> useCase = (IArqCommandPagination<R, P>) applicationContext.getBean(usecaseCamelNotacion);
            if (useCase == null) {
                throw new RuntimeException("El caso de Uso <" + useCaseName + "> no existe");
            }
            return useCase.executeInner(paramObj, pageable);

        } catch (ConstraintViolationException | NotExistException | ArqBussinessRuleException excConstraint) {

            throw excConstraint;

        } catch (Throwable exc) {

            throw new ArqBussinessRuleException(exc.getMessage(), null);
        }
    }

}