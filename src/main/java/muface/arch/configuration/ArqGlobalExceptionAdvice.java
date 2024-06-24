package muface.arch.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import muface.arch.exceptions.ArqBaseOperationsException;
import muface.arch.exceptions.NotExistException;
import muface.arch.utils.ArqConstantMessages;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.UnexpectedTypeException;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class ArqGlobalExceptionAdvice {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ArqBaseOperationsException.class)
    public ResponseEntity<String> handleArqBaseOperationsException(ArqBaseOperationsException ex) {
        String errorMessage = messageSource.getMessage(ex.getCode(), ex.getArgs(), LocaleContextHolder.getLocale());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotExistException.class)
    public ResponseEntity<String> handleNotExistException(NotExistException ex) {

        String errorMessage = messageSource.getMessage(ex.getCode(), ex.getArgs(), LocaleContextHolder.getLocale());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    public ResponseEntity<String> handleIndexOutOfBoundsException(IndexOutOfBoundsException ex) {
                                                                  
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException ex) {
        return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchFieldException.class)
    public ResponseEntity<String> handleNoSuchFieldException(NoSuchFieldException ex) {
        String errorMessage = messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND,
                new Object[]{ex.getCause().getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        String errorMessage = messageSource.getMessage(ArqConstantMessages.ERROR_NOT_FOUND, new Object[]{ex.getMessage()},
            LocaleContextHolder.getLocale());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<String> handleUnexpectedTypeException(UnexpectedTypeException ex) {
                                                                
        String errorMessage = messageSource.getMessage(ArqConstantMessages.ERROR_INTERNAL_SERVER_ERROR,
                new Object[]{ex.getMessage()}, LocaleContextHolder.getLocale());
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String errorMessage = violation.getMessage().startsWith("{") && violation.getMessage().endsWith("}") ?
                    resolveMessage(violation.getMessage().substring(1, violation.getMessage().length() -1), locale) :
                    violation.getMessage();
            String fieldName = violation.getPropertyPath().toString();
            errors.put(fieldName, errorMessage);
        }
        return ResponseEntity.badRequest().body(errors);
    }

    private String resolveMessage(String messageKey, Locale locale) {
        try {
            return messageSource.getMessage(messageKey, null, locale);
        } catch (NoSuchMessageException notMsgFoundExc) {
            return "messageKey: " + messageKey + " no localizada en fichero de propiedades _" +
                    locale.getDisplayLanguage();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = messageSource.getMessage(error, locale);
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}
