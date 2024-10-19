package com.itprotopics.cursos.reactive.movies_review_service.handler;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.itprotopics.cursos.reactive.movies_review_service.domain.Review;
import com.itprotopics.cursos.reactive.movies_review_service.repository.ReviewReactiveRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReviewHandler {

  private final ReviewReactiveRepository reviewReactiveRepository;


  public Mono<ServerResponse> getReviews(ServerRequest request) {

    Optional<String> movieInfoId = request.queryParam("movieInfoId");

    if (movieInfoId.isPresent()) {
      return buildReviewsResponse(
          reviewReactiveRepository.findByMovieInfoId(Long.valueOf(movieInfoId.get())));
    } else {
      return buildReviewsResponse(reviewReactiveRepository.findAll());
    }
  }

  private Mono<ServerResponse> buildReviewsResponse(Flux<Review> reviews) {
    return ServerResponse.ok().body(reviews, Review.class);
  }

  public Mono<ServerResponse> getReviewById(ServerRequest request) {
    return null;
  }

  public Mono<ServerResponse> addReview(ServerRequest request) {
    return request.bodyToMono(Review.class).flatMap(reviewReactiveRepository::save)
        .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
  }

  public Mono<ServerResponse> updateReview(ServerRequest request) {
    String reviewId = request.pathVariable("id");
    Mono<Review> existingReview = reviewReactiveRepository.findById(reviewId);

    return existingReview.flatMap(review -> request.bodyToMono(Review.class).map(reqReview -> {
      review.setComment(reqReview.getComment());
      review.setRating(reqReview.getRating());
      return review;
    })).flatMap(reviewReactiveRepository::save)
        .flatMap(ServerResponse.status(HttpStatus.OK)::bodyValue);
  }

  public Mono<ServerResponse> deleteReview(ServerRequest request) {
    String reviewId = request.pathVariable("id");
    Mono<Review> existingReview = reviewReactiveRepository.findById(reviewId);

    return existingReview.flatMap(review -> reviewReactiveRepository.deleteById(reviewId))
        .then(ServerResponse.noContent().build());
  }
}
