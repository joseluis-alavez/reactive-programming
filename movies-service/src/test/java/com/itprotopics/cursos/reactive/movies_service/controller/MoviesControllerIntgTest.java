package com.itprotopics.cursos.reactive.movies_service.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.itprotopics.cursos.reactive.movies_service.domain.Movie;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 8084) // spin up a httpserver in port 8084
@TestPropertySource(properties = {
    "restClient.moviesInfoUrl=http://localhost:8084/v1/movieinfos",
    "restClient.reviewsUrl=http://localhost:8084/v1/reviews"
})
public class MoviesControllerIntgTest {

  @Autowired
  WebTestClient webTestClient;

  @SuppressWarnings("null")
  @Test
  void retrieveMovieById() {
    // given
    String movieId = "abc";
    stubFor(get(urlEqualTo("/v1/movieinfos/" + movieId))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("movieinfo.json")));

    stubFor(get(urlPathEqualTo("/v1/reviews"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("reviews.json")));

    // when
    webTestClient
        .get()
        .uri("/v1/movies/{id}", movieId)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Movie.class)
        .consumeWith(movieEntityExchangeResult -> {
          var movie = movieEntityExchangeResult.getResponseBody();
          assert Objects.requireNonNull(movie).getReviews().size() == 2;
          assertEquals("Batman Begins", movie.getMovieInfo().getName());
        });

  }

  @SuppressWarnings("null")
  @Test
  void retrieveMovieById_notFound() {
    // given
    String movieId = "abc";
    stubFor(get(urlEqualTo("/v1/movieinfos/" + movieId))
        .willReturn(aResponse()
            .withStatus(404)));

    stubFor(get(urlPathEqualTo("/v1/reviews"))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("reviews.json")));

    // when
    webTestClient
        .get()
        .uri("/v1/movies/{id}", movieId)
        .exchange()
        .expectStatus()
        .is4xxClientError()
        .expectBody(String.class)
        .isEqualTo("There is no movie info available for the passed in Id: abc");

    WireMock.verify(1, getRequestedFor(urlEqualTo("/v1/movieinfos/" + movieId)));

  }

  @SuppressWarnings("null")
  @Test
  void retrieveMovieByI_reviewsNotFound() {
    // given
    String movieId = "abc";
    stubFor(get(urlEqualTo("/v1/movieinfos/" + movieId))
        .willReturn(aResponse()
            .withHeader("Content-Type", "application/json")
            .withBodyFile("movieinfo.json")));

    stubFor(get(urlPathEqualTo("/v1/reviews"))
        .willReturn(aResponse()
            .withStatus(404)));

    // when
    webTestClient
        .get()
        .uri("/v1/movies/{id}", movieId)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Movie.class)
        .consumeWith(movieEntityExchangeResult -> {
          var movie = movieEntityExchangeResult.getResponseBody();
          assert Objects.requireNonNull(movie).getReviews().size() == 0;
          assertEquals("Batman Begins", movie.getMovieInfo().getName());
        });

  }

  @SuppressWarnings("null")
  @Test
  void retrieveMovieById_5xxError() {
    // given
    String movieId = "abc";
    stubFor(get(urlEqualTo("/v1/movieinfos/" + movieId))
        .willReturn(aResponse()
            .withStatus(500)
            .withBody("MovieInfo Service Unavailable")));

    // when
    webTestClient
        .get()
        .uri("/v1/movies/{id}", movieId)
        .exchange()
        .expectStatus()
        .is5xxServerError()
        .expectBody(String.class)
        .isEqualTo("MovieInfo Service Unavailable");

    WireMock.verify(4, getRequestedFor(urlEqualTo("/v1/movieinfos/" + movieId)));

  }

}
