package com.itprotopics.cursos.reactive.movies_review_service.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Review {

  @Id
  private String reviewId;
  private Long movieInfoId;
  private String comment;
  // @Min(value = 0L, message = "rating.negative : rating is negative and please pass a non-negative
  // value")
  private Double rating;
}
