package com.itprotopics.cursos.reactive.movies_service.exception;

public class MoviesInfoServerException extends RuntimeException {
  private String message;

  public MoviesInfoServerException(String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}