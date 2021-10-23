package com.w3w.rm.geo.app.controller.advice;


import com.w3w.rm.geo.app.exception.Invalid3WaWordException;
import com.w3w.rm.geo.app.exception.MissingRequiredDataException;
import com.w3w.rm.geo.app.exception.NoSuggestionsAvailableException;
import com.w3w.rm.geo.app.exception.UnableToRetrieveDataException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class ExceptionControllerAdviceTest {

    @InjectMocks
    ExceptionControllerAdvice exceptionControllerAdvice;

    @Test
    public void testHandleUnableToRetrieveDataException() {
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = exceptionControllerAdvice.handleUnableToRetrieveDataException(new UnableToRetrieveDataException("word", "lang"));
        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertNotNull(serviceErrorResponseEntity.getBody());
        Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE, serviceErrorResponseEntity.getStatusCode());
    }

    @Test
    public void testHandleNoSuggestionAvailableException() {
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = exceptionControllerAdvice.handleNoSuggestionFoundOnFetching(new NoSuggestionsAvailableException());
        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertNotNull(serviceErrorResponseEntity.getBody());
        Assert.assertEquals(HttpStatus.NO_CONTENT, serviceErrorResponseEntity.getStatusCode());
    }

    @Test
    public void testHandleMissingRequiredDataException() {
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = exceptionControllerAdvice.handleInvalid3MissingRequiredData(new MissingRequiredDataException("sdf"));
        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertNotNull(serviceErrorResponseEntity.getBody());
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, serviceErrorResponseEntity.getStatusCode());
    }

    @Test
    public void testHandleInvalid3WaWordException() {
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = exceptionControllerAdvice.handleUnableToRetrieveDataException(new Invalid3WaWordException("sdf"));
        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertNotNull(serviceErrorResponseEntity.getBody());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, serviceErrorResponseEntity.getStatusCode());
    }

    @Test
    public void testHandleGenericUnhandledException() {
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = exceptionControllerAdvice.handleUnexpected(new RuntimeException());
        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertNotNull(serviceErrorResponseEntity.getBody());
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, serviceErrorResponseEntity.getStatusCode());
    }
}