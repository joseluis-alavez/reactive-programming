package com.itprotopics.cursos.reactive.movies_review_service.router;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.ReverseArray;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.itprotopics.cursos.reactive.movies_review_service.domain.Review;
import com.itprotopics.cursos.reactive.movies_review_service.handler.ReviewHandler;
import com.itprotopics.cursos.reactive.movies_review_service.repository.ReviewReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest
@AutoConfigureWebTestClient
@ContextConfiguration(classes = {ReviewRouter.class, ReviewHandler.class})
public class ReviewRouterUnitTest {

  @MockBean
  private ReviewReactiveRepository reviewReactiveRepository;

  @Autowired
  private WebTestClient webTestClient;

  static String REVIEWS_URL = "/v1/reviews";

  @Test
  void testAddReview() {
    // given
    Review review = new Review(null, 1L, "Awesome Movie", 9.0);

    when(reviewReactiveRepository.save(review)).thenReturn(Mono.just(review));

    webTestClient.post().uri(REVIEWS_URL).bodyValue(review).exchange().expectStatus().isCreated()
        .expectBody(Review.class).consumeWith(reviewEntityExchangeResult -> {
          Review savedReview = reviewEntityExchangeResult.getResponseBody();
          assertNotNull(savedReview);
          assertEquals("Awesome Movie", savedReview.getComment());
        });

  }

  @Test
  void testGetReviews() {
    // given
    Review review1 = new Review(null, 1L, "Awesome Movie", 9.0);
    Review review2 = new Review(null, 2L, "Great Film", 8.5);
    Review review3 = new Review(null, 3L, "Excellent Watch", 9.5);
    List<Review> reviewList = Arrays.asList(review1, review2, review3);


    when(reviewReactiveRepository.findAll()).thenReturn(Flux.fromIterable(reviewList));

    webTestClient.get().uri(REVIEWS_URL).exchange().expectStatus().isOk()
        .expectBodyList(Review.class).hasSize(3);
  }

  @Test
  void testGetReviewsByMovieInfoId() {
    // given
    Review review1 = new Review(null, 1L, "Awesome Movie", 9.0);
    Review review2 = new Review(null, 1L, "Great Film", 8.5);
    Review review3 = new Review(null, 1L, "Excellent Watch", 9.5);
    List<Review> reviewList = Arrays.asList(review1, review2, review3);

    when(reviewReactiveRepository.findByMovieInfoId(1L)).thenReturn(Flux.fromIterable(reviewList));

    webTestClient.get().uri(REVIEWS_URL + "?movieInfoId=1").exchange().expectStatus().isOk()
        .expectBodyList(Review.class).hasSize(3);
  }

  @Test
  void testUpdateReview() {
    // given
    Review review = new Review("123", 1L, "Awesome Movie", 9.0);
    Review review2 = new Review("123", 1L, "Awesome Movies", 9.0);

    when(reviewReactiveRepository.findById(review.getReviewId())).thenReturn(Mono.just(review));
    when(reviewReactiveRepository.save(review2)).thenReturn(Mono.just(review2));

    webTestClient.put().uri(REVIEWS_URL + "/{id}", review.getReviewId()).bodyValue(review2)
        .exchange().expectStatus().isOk();
  }

  @Test
  void testDeleteReview() {
    // given
    Review review = new Review("123", 1L, "Awesome Movie", 9.0);

    when(reviewReactiveRepository.findById(review.getReviewId())).thenReturn(Mono.just(review));
    when(reviewReactiveRepository.deleteById(review.getReviewId())).thenReturn(Mono.empty());

    webTestClient.delete().uri(REVIEWS_URL + "/{id}", review.getReviewId()).exchange()
        .expectStatus().isNoContent();
  }

}
