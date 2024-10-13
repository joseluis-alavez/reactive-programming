package com.itprotopics.cursos.reactive.movies_info_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.itprotopics.cursos.reactive.movies_info_service.domain.MovieInfo;
import com.itprotopics.cursos.reactive.movies_info_service.service.MoviesInfoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class MoviesInfoController {

  private final MoviesInfoService moviesInfoService;

  @GetMapping("/movieinfos")
  @ResponseStatus(HttpStatus.OK)
  public Flux<MovieInfo> getMovieInfos(
      @RequestParam(value = "year", required = false) Integer year) {
    if (year != null) {
      return moviesInfoService.getMovieInfosByYear(year).log();
    }
    return moviesInfoService.getAllMovieInfos().log();
  }


  @PostMapping("/movieinfos")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<MovieInfo> addMovieInfo(@RequestBody @Valid MovieInfo movieInfo) {
    return moviesInfoService.addMovieInfo(movieInfo).log();
  }

  @GetMapping("/movieinfos/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseEntity<MovieInfo>> getMovieInfoById(@PathVariable String id) {
    return moviesInfoService.getMovieInfoById(id).map(ResponseEntity.ok()::body)
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())).log();
  }

  @PutMapping("/movieinfos/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Mono<ResponseEntity<MovieInfo>> updateMovieInfo(@RequestBody MovieInfo movieInfo,
          @PathVariable String id) {
    return moviesInfoService.updateMovieInfo(movieInfo, id).map(ResponseEntity.ok()::body)
        .switchIfEmpty(Mono.just(ResponseEntity.notFound().build())).log();
  }

  @DeleteMapping("/movieinfos/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deleteMovieInfo(@PathVariable String id) {
    return moviesInfoService.deleteMovieInfo(id).log();
  }


}
