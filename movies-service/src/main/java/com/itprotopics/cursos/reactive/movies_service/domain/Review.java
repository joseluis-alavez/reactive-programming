package com.itprotopics.cursos.reactive.movies_service.domain;

import lombok.Data;

@Data
public class Review {

  private String reviewId;

  private Long movieInfoId;
  private String comment;

  private Double rating;

}