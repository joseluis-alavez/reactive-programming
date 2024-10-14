package com.itprotopics.cursos.reactive.movies_review_service.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.itprotopics.cursos.reactive.movies_review_service.domain.Review;
import com.itprotopics.cursos.reactive.movies_review_service.repository.ReviewReactiveRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReviewHandler {

  private final ReviewReactiveRepository reviewReactiveRepository;

  public Mono<ServerResponse> getReviews(ServerRequest request) {
    return null;
  }

  public Mono<ServerResponse> getReviewById(ServerRequest request) {
    return null;
  }

  public Mono<ServerResponse> addReview(ServerRequest request) {
    return request.bodyToMono(Review.class).flatMap(reviewReactiveRepository::save)
        .flatMap(ServerResponse.status(HttpStatus.CREATED)::bodyValue);
  }

  public Mono<ServerResponse> updateReview(ServerRequest request) {
    return null;
  }

  public Mono<ServerResponse> deleteReview(ServerRequest request) {
    return null;
  }

}
