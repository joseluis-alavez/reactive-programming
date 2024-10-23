package com.itprotopics.cursos.reactive.movies_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.itprotopics.cursos.reactive.movies_service.domain.Review;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@AllArgsConstructor
public class ReviewsRestClient {

  private WebClient webClient;

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
        .bodyToFlux(Review.class)
        .log();
  }

}