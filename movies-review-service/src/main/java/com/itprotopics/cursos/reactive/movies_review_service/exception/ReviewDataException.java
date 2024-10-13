package com.itprotopics.cursos.reactive.movies_review_service.exception;

public class ReviewDataException extends RuntimeException {
  private String message;

  public ReviewDataException(String s) {
    super(s);
    this.message = s;
  }
}
