package com.payconiq.rm.stocks.app.controller.advice;

import com.payconiq.rm.stocks.app.exception.*;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public class ExceptionControllerAdviceTest {

    @Test
    public void testHandleDuplicateStockException() {
        ExceptionControllerAdvice advice = new ExceptionControllerAdvice();

        DuplicateStockException exception = new DuplicateStockException("stock1");
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = advice.handleDuplicateStockException(exception);

        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertEquals(HttpStatus.ALREADY_REPORTED, serviceErrorResponseEntity.getStatusCode());
        ExceptionControllerAdvice.ServiceError body = serviceErrorResponseEntity.getBody();
        Assert.assertNotNull(body);
        Assert.assertEquals(exception.getMessage(), body.getMessage());
        Assert.assertEquals(exception.getCause(), body.getDetails());
    }


    @Test
    public void testHandleInvalidCreateRequestException() {
        ExceptionControllerAdvice advice = new ExceptionControllerAdvice();

        InvalidCreateRequestException exception = new InvalidCreateRequestException();
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = advice.handleInvalidCreateRequestException(exception);

        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, serviceErrorResponseEntity.getStatusCode());
        ExceptionControllerAdvice.ServiceError body = serviceErrorResponseEntity.getBody();
        Assert.assertNotNull(body);
        Assert.assertEquals(exception.getMessage(), body.getMessage());
        Assert.assertEquals(exception.getCause(), body.getDetails());
    }

    @Test
    public void testHandleInvalidUpdateRequestException() {
        ExceptionControllerAdvice advice = new ExceptionControllerAdvice();

        InvalidUpdateRequestException exception = new InvalidUpdateRequestException();
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = advice.handleInvalidUpdateRequestException(exception);

        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, serviceErrorResponseEntity.getStatusCode());
        ExceptionControllerAdvice.ServiceError body = serviceErrorResponseEntity.getBody();
        Assert.assertNotNull(body);
        Assert.assertEquals(exception.getMessage(), body.getMessage());
        Assert.assertEquals(exception.getCause(), body.getDetails());
    }

    @Test
    public void testHandleStockNotFoundException() {
        ExceptionControllerAdvice advice = new ExceptionControllerAdvice();

        StockNotFoundException exception = new StockNotFoundException(12);
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = advice.handleStockNotFoundException(exception);

        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertEquals(HttpStatus.NO_CONTENT, serviceErrorResponseEntity.getStatusCode());
        ExceptionControllerAdvice.ServiceError body = serviceErrorResponseEntity.getBody();
        Assert.assertNotNull(body);
        Assert.assertEquals(exception.getMessage(), body.getMessage());
        Assert.assertEquals(exception.getCause(), body.getDetails());
    }


    @Test
    public void testHandleLockWindowEnabledException() {
        ExceptionControllerAdvice advice = new ExceptionControllerAdvice();

        LockWindowEnabledException exception = new LockWindowEnabledException();
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = advice.handleLockWindowEnabledException(exception);

        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertEquals(HttpStatus.FORBIDDEN, serviceErrorResponseEntity.getStatusCode());
        ExceptionControllerAdvice.ServiceError body = serviceErrorResponseEntity.getBody();
        Assert.assertNotNull(body);
        Assert.assertEquals(exception.getMessage(), body.getMessage());
        Assert.assertEquals(exception.getCause(), body.getDetails());
    }


    @Test
    public void testHandleUnexpected() {
        ExceptionControllerAdvice advice = new ExceptionControllerAdvice();

        Exception exception = new Exception();
        ResponseEntity<ExceptionControllerAdvice.ServiceError> serviceErrorResponseEntity = advice.handleUnexpected(exception);

        Assert.assertNotNull(serviceErrorResponseEntity);
        Assert.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, serviceErrorResponseEntity.getStatusCode());
        ExceptionControllerAdvice.ServiceError body = serviceErrorResponseEntity.getBody();
        Assert.assertNotNull(body);
        Assert.assertEquals(exception.getMessage(), body.getMessage());
        Assert.assertEquals(exception, body.getDetails());
    }

}