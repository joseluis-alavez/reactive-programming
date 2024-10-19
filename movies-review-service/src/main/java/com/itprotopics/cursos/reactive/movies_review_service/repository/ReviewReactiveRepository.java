package com.itprotopics.cursos.reactive.movies_review_service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.itprotopics.cursos.reactive.movies_review_service.domain.Review;
import reactor.core.publisher.Flux;

public interface ReviewReactiveRepository extends ReactiveMongoRepository<Review, String> {
  Flux<Review> findByMovieInfoId(Long movieInfoId);
}
