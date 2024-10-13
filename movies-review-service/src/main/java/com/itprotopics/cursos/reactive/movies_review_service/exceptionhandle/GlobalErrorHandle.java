package com.itprotopics.cursos.reactive.movies_review_service.exceptionhandle;

import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandle {

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<String> handleRequestBodyError(WebExchangeBindException ex) {
    log.error("Exception Caught in handleRequestBodyError :  {} ", ex.getMessage(), ex);

    String error = ex.getBindingResult().getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).sorted()
        .collect(Collectors.joining(", "));

    log.error("Error is : {}", error);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}
