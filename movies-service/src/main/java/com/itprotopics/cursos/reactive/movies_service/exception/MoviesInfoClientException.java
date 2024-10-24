package com.itprotopics.cursos.reactive.movies_service.exception;

public class MoviesInfoClientException extends RuntimeException {

  private String message;
  private Integer statusCode;

  public MoviesInfoClientException(String message, Integer statusCode) {
    super(message);
    this.statusCode = statusCode;
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

}
