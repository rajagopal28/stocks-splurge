package com.payconiq.rm.stocks.app.controller.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.payconiq.rm.stocks.app.exception.*;
import com.payconiq.rm.stocks.app.util.StockAppUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
@ControllerAdvice
public class ExceptionControllerAdvice {

    /**
     * Advice Handler to process DuplicateStockException thrown at runtime
     *
     * @return ResponseEntity with ALREADY_REPORTED status code to indicate the stock with the given name is present already.
     */
    @ExceptionHandler(DuplicateStockException.class)
    public ResponseEntity<ServiceError> handleDuplicateStockException(DuplicateStockException exception) {
        log.error("Request processing failed as there were duplicate data!", exception);
        return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(ServiceError.from(exception));
    }

    /**
     * Advice Handler to process InvalidCreateRequestException thrown at runtime
     *
     * @return ResponseEntity with UNPROCESSABLE_ENTITY status code to indicate the stock request had some required data missing.
     */
    @ExceptionHandler(InvalidCreateRequestException.class)
    public ResponseEntity<ServiceError> handleInvalidCreateRequestException(InvalidCreateRequestException exception) {
        log.error("Request processing failed as the fields are not valid!", exception);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ServiceError.from(exception));
    }

    /**
     * Advice Handler to process InvalidUpdateRequestException thrown at runtime
     *
     * @return ResponseEntity with BAD_REQUEST status code to indicate the update stock request is empty.
     */
    @ExceptionHandler(InvalidUpdateRequestException.class)
    public ResponseEntity<ServiceError> handleInvalidUpdateRequestException(InvalidUpdateRequestException exception) {
        log.error("Request processing failed as the fields are not valid!", exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ServiceError.from(exception));
    }

    /**
     * Advice Handler to process StockNotFoundException thrown at runtime
     *
     * @return ResponseEntity with NO_CONTENT status code to indicate the stock is not found for the update/delete request sent.
     */
    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<ServiceError> handleStockNotFoundException(StockNotFoundException exception) {
        log.error("Request processing failed as requested stock id is not found!", exception);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ServiceError.from(exception));
    }

    /**
     * Advice Handler to process LockWindowEnabledException thrown at runtime
     *
     * @return ResponseEntity with FORBIDDEN status code to indicate the update/delete request is sent for stock in lock window.
     */
    @ExceptionHandler(LockWindowEnabledException.class)
    public ResponseEntity<ServiceError> handleLockWindowEnabledException(LockWindowEnabledException exception) {
        log.error("Request processing failed as the update/delete is tried within the lock window!", exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ServiceError.from(exception));
    }

    /**
     * Advice Handler to process Generic exceptions that are thrown at runtime
     *
     * @return ResponseEntity with INTERNAL_SERVER_ERROR status code to indicate the there is a runtime failure
     * */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceError> handleUnexpected(Exception exception) {
        log.error("Unexpected exception", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ServiceError(exception.getMessage(), exception));
    }


    @Data
    public static class ServiceError {
        @NotNull
        private final String message;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private final Object details;

        public static ServiceError from(RuntimeException ex) {
            return new ServiceError(ex.getMessage(), ex.getCause());
        }

        public static ServiceError from(MethodArgumentNotValidException ex) {
            return new ServiceError(StockAppUtil.ERROR_REQUEST_VALIDATION_FAILED, ex.getBindingResult().getAllErrors());
        }
    }
}
