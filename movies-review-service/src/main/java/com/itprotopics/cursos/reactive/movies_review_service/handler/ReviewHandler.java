package com.itprotopics.cursos.reactive.movies_review_service.handler;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import com.itprotopics.cursos.reactive.movies_review_service.domain.Review;
import com.itprotopics.cursos.reactive.movies_review_service.exception.ReviewDataException;
import com.itprotopics.cursos.reactive.movies_review_service.exception.ReviewNotFoundException;
import com.itprotopics.cursos.reactive.movies_review_service.repository.ReviewReactiveRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewHandler {

  private final ReviewReactiveRepository reviewReactiveRepository;

  private final Validator validator;

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
    return request.bodyToMono(Review.class)
        .doOnNext(this::validate)
        .flatMap(reviewReactiveRepository::save)
        .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
  }

  private void validate(Review review) {
    Set<ConstraintViolation<Review>> violations = validator.validate(review);
    log.info("Violations: {}", violations);
    if (!violations.isEmpty()) {
      String errorMessage = violations.stream()
          .map(ConstraintViolation::getMessage)
          .sorted().collect(Collectors.joining(", "));
      throw new ReviewDataException(errorMessage);
    }
  }

  public Mono<ServerResponse> updateReview(ServerRequest request) {
    String reviewId = request.pathVariable("id");
    Mono<Review> existingReview = reviewReactiveRepository.findById(reviewId)
        .switchIfEmpty(Mono.error(new ReviewNotFoundException("Review not found for the given review id " + reviewId)));

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
