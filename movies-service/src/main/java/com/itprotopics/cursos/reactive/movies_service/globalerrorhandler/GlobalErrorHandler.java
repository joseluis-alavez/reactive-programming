package com.itprotopics.cursos.reactive.movies_service.globalerrorhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.itprotopics.cursos.reactive.movies_service.exception.MoviesInfoClientException;
import com.itprotopics.cursos.reactive.movies_service.exception.MoviesInfoServerException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandler {

  @ExceptionHandler(MoviesInfoClientException.class)
  public ResponseEntity<String> handleMoviesInfoClientException(MoviesInfoClientException exception) {
    log.error("Exception Caught in handleMoviesInfoClientException : {}", exception.getMessage());
    return ResponseEntity.status(HttpStatus.valueOf(exception.getStatusCode())).body(exception.getMessage());
  }

  @ExceptionHandler(MoviesInfoServerException.class)
  public ResponseEntity<String> handleMoviesInfoServerException(MoviesInfoServerException exception) {
    log.error("Exception Caught in handleMoviesInfoServerException : {}", exception.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
  }
}
