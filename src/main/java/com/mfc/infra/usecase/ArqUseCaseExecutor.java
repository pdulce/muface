package com.mfc.infra.usecase;

import com.mfc.infra.command.ArqAbstractUseCase;
import com.mfc.infra.command.ArqAbstractUseCasePagination;
import com.mfc.infra.exceptions.ArqBussinessRuleException;
import com.mfc.infra.exceptions.NotExistException;
import com.mfc.infra.utils.ArqConversionUtils;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class ArqUseCaseExecutor {

    @Autowired
    ApplicationContext applicationContext;

    public <R, P> R executeUseCase(String useCaseName, P paramObj) {
        try {
            Class<?> useCaseClass = Class.forName(useCaseName);
            ArqAbstractUseCase<R, P> useCase = (ArqAbstractUseCase<R, P>) applicationContext.getBean(useCaseClass);
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
            Class<?> useCaseClass = Class.forName(useCaseName);
            ArqAbstractUseCasePagination<R, P> useCase =
                    (ArqAbstractUseCasePagination<R, P>) applicationContext.getBean(useCaseClass);
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