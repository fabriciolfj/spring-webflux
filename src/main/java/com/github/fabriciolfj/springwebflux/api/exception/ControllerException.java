package com.github.fabriciolfj.springwebflux.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerException {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException ex) {
        log.error("Exception caught in handleRuntimeException: {}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
