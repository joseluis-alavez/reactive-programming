package com.itprotopics.cursos.reactive.movies_service.exception;

public class ReviewsClientException extends RuntimeException {

  private String message;

  public ReviewsClientException(String message) {
    super(message);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
