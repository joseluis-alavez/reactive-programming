package com.itprotopics.cursos.reactive.movies_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.itprotopics.cursos.reactive.movies_service.domain.MovieInfo;
import com.itprotopics.cursos.reactive.movies_service.exception.MoviesInfoClientException;
import com.itprotopics.cursos.reactive.movies_service.exception.MoviesInfoServerException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
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
        .onStatus(status -> status.is4xxClientError(), clientResponse -> {
          log.info("Status code is : {}", clientResponse.statusCode());
          if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
            return Mono
                .error(
                    new MoviesInfoClientException("There is no movie info available for the passed in Id: " + movieId,
                        clientResponse.statusCode().value()));
          }
          return clientResponse.bodyToMono(String.class)
              .flatMap(responseBody -> Mono
                  .error(new MoviesInfoClientException(responseBody, clientResponse.statusCode().value())));
        })
        .onStatus(status -> status.is5xxServerError(), clientResponse -> {
          log.info("Status code is : {}", clientResponse.statusCode());
          return clientResponse.bodyToMono(String.class)
              .flatMap(responseBody -> Mono
                  .error(new MoviesInfoServerException(responseBody)));
        })
        .bodyToMono(MovieInfo.class)
        .log();
  }

}
