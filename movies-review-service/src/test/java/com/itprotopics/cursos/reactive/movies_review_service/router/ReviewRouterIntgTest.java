package com.itprotopics.cursos.reactive.movies_review_service.router;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.itprotopics.cursos.reactive.movies_review_service.domain.Review;
import com.itprotopics.cursos.reactive.movies_review_service.repository.ReviewReactiveRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ReviewRouterIntgTest {

  static final String REVIEWS_URL = "/v1/reviews";

  @Autowired
  private WebTestClient webTestClient;

  @Autowired
  ReviewReactiveRepository reviewReactiveRepository;

  @BeforeEach
  void setUp() {
    List<Review> reviewsList = List.of(new Review(null, 1L, "Awesome Movie", 9.0),
        new Review("123", 1L, "Awesome Movie1", 9.0), new Review(null, 2L, "Excellent Movie", 8.0));
    reviewReactiveRepository.saveAll(reviewsList).blockLast();
  }

  @AfterEach
  void tearDown() {
    reviewReactiveRepository.deleteAll().block();
  }

  @Test
  void testReviewsRoute() {
    // given
    Review review = new Review(null, 1L, "Awesome Movie", 9.0);

    // when
    webTestClient.post().uri(REVIEWS_URL).bodyValue(review).exchange().expectStatus().isCreated()
        .expectBody(Review.class).consumeWith(reviewEntityExchangeResult -> {
          Review result = reviewEntityExchangeResult.getResponseBody();
          assertNotNull(result);
          assertNotNull(result.getReviewId());
          assertEquals(review.getMovieInfoId(), result.getMovieInfoId());
          assertEquals(review.getComment(), result.getComment());
          assertEquals(review.getRating(), result.getRating());
        });

  }

  @Test
  void testGetReviews() {
    // when
    webTestClient.get().uri(REVIEWS_URL).exchange().expectStatus().isOk()
        .expectBodyList(Review.class).hasSize(3);
  }

  @Test
  void testGetReviewsByMovieInfoId() {
    // when
    webTestClient.get().uri(REVIEWS_URL + "?movieInfoId=1").exchange().expectStatus().isOk()
        .expectBodyList(Review.class).hasSize(2);
  }

  @Test
  void testUpdateReview() {
    // given
    Review review = new Review(null, 1L, "Updated Movie", 8.5);

    // when
    webTestClient.put().uri(REVIEWS_URL + "/123").bodyValue(review).exchange().expectStatus().isOk()
        .expectBody(Review.class).consumeWith(reviewEntityExchangeResult -> {
          Review updatedReview = reviewEntityExchangeResult.getResponseBody();
          assertNotNull(updatedReview);
          assertEquals("123", updatedReview.getReviewId());
          assertEquals(1L, updatedReview.getMovieInfoId());
          assertEquals("Updated Movie", updatedReview.getComment());
          assertEquals(8.5, updatedReview.getRating());
        });
  }

  @Test
  void testDeleteReview() {
    // when
    webTestClient.delete().uri(REVIEWS_URL + "/123").exchange().expectStatus().isNoContent();
  }

}
