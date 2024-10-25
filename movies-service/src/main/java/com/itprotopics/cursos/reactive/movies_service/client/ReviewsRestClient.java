package com.itprotopics.cursos.reactive.movies_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.itprotopics.cursos.reactive.movies_service.domain.Review;
import com.itprotopics.cursos.reactive.movies_service.exception.ReviewsClientException;
import com.itprotopics.cursos.reactive.movies_service.exception.ReviewsServerException;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReviewsRestClient {

  private WebClient webClient;

  public ReviewsRestClient(WebClient webClient) {
    this.webClient = webClient;
  }

  @Value("${restClient.reviewsUrl}")
  private String reviewsUrl;

  public Flux<Review> retrieveReviews(String movieId) {

    String url = UriComponentsBuilder
        .fromUriString(reviewsUrl)
        .queryParam("movieInfoId", movieId)
        .buildAndExpand()
        .toUriString();

    return webClient
        .get()
        .uri(url)
        .retrieve()
        .onStatus(status -> status.is4xxClientError(), clientResponse -> {
          log.info("Status code is : {}", clientResponse.statusCode());
          if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
            return Mono.empty();
          }
          return clientResponse.bodyToMono(String.class)
              .flatMap(responseBody -> Mono
                  .error(new ReviewsClientException(responseBody)));
        })
        .onStatus(status -> status.is5xxServerError(), clientResponse -> {
          log.info("Status code is : {}", clientResponse.statusCode());
          return clientResponse.bodyToMono(String.class)
              .flatMap(responseBody -> Mono
                  .error(new ReviewsServerException("Server Exception in ReviewsService " + responseBody)));
        })
        .bodyToFlux(Review.class)
        .log();
  }

}