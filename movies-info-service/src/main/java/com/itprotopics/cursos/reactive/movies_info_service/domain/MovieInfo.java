package com.itprotopics.cursos.reactive.movies_info_service.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document
public class MovieInfo {

  @Id
  private String movieInfoId;
  private String name;
  private Integer year;
  private List<String> cast;
  private LocalDate releaseDate;

}
