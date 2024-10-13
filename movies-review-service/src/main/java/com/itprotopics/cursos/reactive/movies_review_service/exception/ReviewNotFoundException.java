package com.itprotopics.cursos.reactive.movies_review_service.exception;

public class ReviewNotFoundException extends RuntimeException {

  private String message;
  private Throwable ex;

  public ReviewNotFoundException(String message, Throwable ex) {
    super(message, ex);
    this.message = message;
    this.ex = ex;
  }

  public ReviewNotFoundException(String message) {
    super(message);
    this.message = message;
  }
}