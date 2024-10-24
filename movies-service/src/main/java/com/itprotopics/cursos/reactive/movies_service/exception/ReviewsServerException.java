package com.itprotopics.cursos.reactive.movies_service.exception;

public class ReviewsServerException extends RuntimeException {
  private String message;

  public ReviewsServerException(String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

}
