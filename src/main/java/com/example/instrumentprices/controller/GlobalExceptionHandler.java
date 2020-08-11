package com.example.instrumentprices.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Handles application exceptions. This class should be extended by another methods to handle different kind of exceptions
 * like Authorization, Validation and etc. In all the methods we can return more specific class like ErrorResponse which
 * will contain more details about exception.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    public static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ResponseEntity<Object>("Internal server error", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
