package com.itprotopics.cursos.reactive.movies_service.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

  private MovieInfo movieInfo;
  private List<Review> reviews;

}