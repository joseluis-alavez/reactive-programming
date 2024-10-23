package com.itprotopics.cursos.reactive.movies_service.domain;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieInfo {
  private String movieInfoId;
  @NotBlank(message = "movieInfo.name must be present")
  private String name;
  @NotNull(message = "movieInfo.year must be present")
  private Integer year;
  private List<@NotBlank(message = "movieInfo.cast must be present") String> cast;
  private LocalDate releaseDate;
}
