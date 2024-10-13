package com.itprotopics.cursos.reactive.movies_info_service.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document
public class MovieInfo {

  @Id
  private String movieInfoId;
  @NotBlank(message = "Name is required")
  private String name;
  @NotNull(message = "Year is required")
  @Positive(message = "Year must be greater than 0")
  private Integer year;
  private List<@NotBlank(message = "Cast must be non empty") String> cast;
  private LocalDate releaseDate;

}
