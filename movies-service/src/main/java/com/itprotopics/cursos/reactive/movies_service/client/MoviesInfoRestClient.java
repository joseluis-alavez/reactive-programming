package com.itprotopics.cursos.reactive.movies_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.itprotopics.cursos.reactive.movies_service.domain.MovieInfo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@AllArgsConstructor
public class MoviesInfoRestClient {

  private WebClient webClient;

  @Value("${restClient.moviesInfoUrl}")
  private String moviesInfoUrl;

  public Mono<MovieInfo> retrieveMovieInfo(String movieId) {

    String url = UriComponentsBuilder
        .fromUriString(moviesInfoUrl)
        .path("/{id}")
        .buildAndExpand(movieId)
        .toUriString();

    return webClient
        .get()
        .uri(url)
        .retrieve()
        .bodyToMono(MovieInfo.class)
        .log();
  }

}
