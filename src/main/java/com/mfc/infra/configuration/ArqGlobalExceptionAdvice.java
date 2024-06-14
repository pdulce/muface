package com.mfc.infra.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mfc.infra.exceptions.ArqNotExistException;
import com.mfc.infra.utils.ArqConstantMessages;
import jakarta.validation.ConstraintViolationException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ArqGlobalExceptionAdvice {

    Logger logger = LoggerFactory.getLogger(ArqGlobalExceptionAdvice.class);

    @Autowired
    private MessageSource messageSource;


    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<String> handleResourceNotFoundException(IndexOutOfBoundsException ex,
                                                                  WebRequest request, Locale locale) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleResourceNotFoundException(IllegalArgumentException ex,
                                                                  WebRequest request, Locale locale) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleResourceNotFoundException(JsonProcessingException ex,
                                                                  WebRequest request, Locale locale) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ConstraintViolationException ex,
                                                                  WebRequest request, Locale locale) {
        // Manejamos todas las excepciones que tienen que ver con la validaicón sintáctica de cualquier mapeo de entidad
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchFieldException.class)
    public ResponseEntity<String> handleResourceNotFoundException(NoSuchFieldException ex,
                                                                  WebRequest request, Locale locale) {
        String errorMessage = messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND,
                new Object[]{ex.getCause().getMessage()}, locale);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ArqNotExistException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ArqNotExistException ex,
                                                                  WebRequest request, Locale locale) {
        String errorMessage = messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND,
                new Object[]{ex.getMsgError()}, locale);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                  WebRequest request, Locale locale) {
        String errorMessage = messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND, new Object[]{ex.getMessage()},
                locale);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        logger.error("some errors: " + errors.size());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        logger.error("some errors: " + errors.size());
        return errorResponse;
    }

}
