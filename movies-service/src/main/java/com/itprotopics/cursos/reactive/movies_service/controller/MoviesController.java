package com.itprotopics.cursos.reactive.movies_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itprotopics.cursos.reactive.movies_service.client.MoviesInfoRestClient;
import com.itprotopics.cursos.reactive.movies_service.client.ReviewsRestClient;
import com.itprotopics.cursos.reactive.movies_service.domain.Movie;
import com.itprotopics.cursos.reactive.movies_service.domain.Review;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/movies")
@AllArgsConstructor
public class MoviesController {

  private final MoviesInfoRestClient moviesInfoRestClient;

  private final ReviewsRestClient reviewsRestClient;

  @GetMapping("/{id}")
  public Mono<Movie> retrieveMovieById(@PathVariable String movieId) {

    return moviesInfoRestClient.retrieveMovieInfo(movieId)
        .flatMap(movieInfo -> {
          Mono<List<Review>> reviewsListMono = reviewsRestClient.retrieveReviews(movieId)
              .collectList();
          return reviewsListMono.map(reviews -> new Movie(movieInfo, reviews));
        });

  }

}