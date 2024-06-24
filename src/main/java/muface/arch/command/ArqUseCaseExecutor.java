package muface.arch.command;

import muface.arch.exceptions.ArqBussinessRuleException;
import muface.arch.exceptions.NotExistException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
            ArqAbstractUseCase<R, P> useCase = (ArqAbstractUseCase<R, P>) applicationContext.getBean(usecaseCamelNotacion);
            if (useCase == null) {
                throw new RuntimeException("El caso de Uso <" + useCaseName + "> no existe");
            }
            return useCase.execute(paramObj);

        } catch (ConstraintViolationException | NotExistException | ArqBussinessRuleException excConstraint) {

            throw excConstraint;

        } catch (Throwable exc) {

            throw new ArqBussinessRuleException(exc.getMessage(), null);
        }
    }

    public <R, P> R executePaginationUseCase(String useCaseName, P paramObj, Pageable pageable) {
        try {
            ArqAbstractUseCasePagination<R, P> useCase = (ArqAbstractUseCasePagination<R, P>)
                    applicationContext.getBean(useCaseName);
            if (useCase == null) {
                throw new RuntimeException("El caso de Uso <" + useCaseName + "> no existe");
            }
            return useCase.executeQueryPaginada(paramObj, pageable);

        } catch (ConstraintViolationException | NotExistException | ArqBussinessRuleException excConstraint) {

            throw excConstraint;

        } catch (Throwable exc) {

            throw new ArqBussinessRuleException(exc.getMessage(), null);
        }
    }

}