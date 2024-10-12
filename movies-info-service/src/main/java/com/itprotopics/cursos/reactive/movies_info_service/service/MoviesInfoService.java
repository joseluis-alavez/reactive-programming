package com.itprotopics.cursos.reactive.movies_info_service.service;

import org.springframework.stereotype.Service;
import com.itprotopics.cursos.reactive.movies_info_service.domain.MovieInfo;
import com.itprotopics.cursos.reactive.movies_info_service.repository.MovieInfoRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class MoviesInfoService {

  private final MovieInfoRepository movieInfoRepository;

  public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
    return movieInfoRepository.save(movieInfo);
  }

  public Flux<MovieInfo> getAllMovieInfos() {
    return movieInfoRepository.findAll();
  }

  public Mono<MovieInfo> getMovieInfoById(String id) {
    return movieInfoRepository.findById(id);
  }

  public Mono<MovieInfo> updateMovieInfo(MovieInfo updateMovieInfo, String id) {
    return movieInfoRepository.findById(id).flatMap(movieInfo -> {
      movieInfo.setName(updateMovieInfo.getName());
      movieInfo.setYear(updateMovieInfo.getYear());
      movieInfo.setCast(updateMovieInfo.getCast());
      movieInfo.setReleaseDate(updateMovieInfo.getReleaseDate());
      return movieInfoRepository.save(movieInfo);
    });
  }

  public Mono<Void> deleteMovieInfo(String id) {
    return movieInfoRepository.deleteById(id);
  }

}
