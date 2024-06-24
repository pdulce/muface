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
                throw new ClassNotFoundException(useCaseName);
            }
            return useCase.execute(paramObj);

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);

        } catch (ConstraintViolationException excConstraint) {

            throw excConstraint;

        } catch (NotExistException notExistException) {

            throw notExistException;

        } catch (ArqBussinessRuleException arqBussinessRuleException) {

            throw arqBussinessRuleException;

        } catch (Throwable exc) {

            throw new ArqBussinessRuleException(exc.getMessage(), null);
        }
    }

    public <R, P> R executePaginationUseCase(String useCaseName, P paramObj, Pageable pageable) {
        try {
            //Class<?> useCaseClass = Class.forName(useCaseName);
            String usecaseCamelNotacion = useCaseName.substring(0,1).toLowerCase().concat(useCaseName.substring(1));
            ArqAbstractUseCasePagination<R, P> useCase =
                    (ArqAbstractUseCasePagination<R, P>) applicationContext.getBean(usecaseCamelNotacion);
            if (useCase == null) {
                throw new ClassNotFoundException(useCaseName);
            }
            return useCase.executeQueryPaginada(paramObj, pageable);

        } catch (ClassNotFoundException e) {

            throw new RuntimeException(e);

        } catch (ConstraintViolationException excConstraint) {

            throw excConstraint;

        } catch (NotExistException notExistException) {

            throw notExistException;

        } catch (ArqBussinessRuleException arqBussinessRuleException) {

            throw arqBussinessRuleException;

        } catch (Throwable exc) {

            throw new ArqBussinessRuleException(exc.getMessage(), null);
        }
    }

}