package com.w3w.rm.geo.app.controller.advice;

import com.w3w.rm.geo.app.exception.Invalid3WaWordException;
import com.w3w.rm.geo.app.exception.MissingRequiredDataException;
import com.w3w.rm.geo.app.exception.NoSuggestionsAvailableException;
import com.w3w.rm.geo.app.exception.UnableToRetrieveDataException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
/**
 *
 * Exception Controller Advice takes care of all the runtime exceptions thrown at the controller level
 * and converts them to proper response with appropriate status codes
 *
 * @author Rajagopal
 *
 */
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    /**
     * Advice Handler to process Invalid3WaWordException thrown at runtime
     *
     * @return ResponseEntity with BAD_REQUEST status code to indicate the 3wa passed is invalid format
     */
    @ExceptionHandler(Invalid3WaWordException.class)
    public ResponseEntity<ServiceError> handleUnableToRetrieveDataException(Invalid3WaWordException exception) {
        log.error("Failed 3wa processing for invalid 3wa string input!", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ServiceError.from(exception));
    }

    /**
     * Advice Handler to process MissingRequiredDataException thrown at runtime
     *
     * @return ResponseEntity with UNPROCESSABLE_ENTITY status code to indicate the data passed is invalid
     */
    @ExceptionHandler(Invalid3WaWordException.class)
    public ResponseEntity<ServiceError> handleInvalid3MissingRequiredData(MissingRequiredDataException exception) {
        log.error("Invalid input passed or essential information missing!", exception);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ServiceError.from(exception));
    }

    /**
     * Advice Handler to process NoSuggestionsAvailableException thrown at runtime
     *
     * @return ResponseEntity with NO_CONTENT status code to indicate the no 3wa suggestions found for given string
     */
    @ExceptionHandler(Invalid3WaWordException.class)
    public ResponseEntity<ServiceError> handleNoSuggestionFoundOnFetching(NoSuggestionsAvailableException exception) {
        log.error("Failed to fetch suggestions or no suggestion available for given 3wa!", exception);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ServiceError.from(exception));
    }

    /**
     * Advice Handler to process UnableToRetrieveDataException thrown at runtime
     *
     * @return ResponseEntity with SERVICE_UNAVAILABLE status code to indicate the 3wa API call is failing
     */
    @ExceptionHandler(Invalid3WaWordException.class)
    public ResponseEntity<ServiceError> handleUnableToRetrieveDataException(UnableToRetrieveDataException exception) {
        log.error("Failed to invoke API as it throws NullPointerException!", exception);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ServiceError.from(exception));
    }

    /**
     * Advice Handler to process Generic exceptions that are thrown at runtime
     *
     * @return ResponseEntity with INTERNAL_SERVER_ERROR status code to indicate the there is a runtime failure
     * */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleExceptionInternal(ex, ServiceError.from(ex), headers, status, request);
    }

    /**
     * Advice Handler to process Generic exceptions that are thrown at runtime
     *
     * @return ResponseEntity with INTERNAL_SERVER_ERROR status code to indicate the there is a runtime failure
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceError> handleUnexpected(Exception exception) {
        log.error("Unexpected exception", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceError(exception.getMessage(), null));
    }

    @Data
    public static class ServiceError {
        @NotNull
        private final String errorMessage;
        private final Object details;

        public static ServiceError from(RuntimeException paymentException) {
            return new ServiceError(paymentException.getMessage(), paymentException.getCause());
        }

        public static ServiceError from(MethodArgumentNotValidException ex) {
            return new ServiceError("Validation failed", ex.getBindingResult().getAllErrors());
        }
    }
}
