package com.itprotopics.cursos.reactive.movies_info_service.controller;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.itprotopics.cursos.reactive.movies_info_service.domain.MovieInfo;
import com.itprotopics.cursos.reactive.movies_info_service.repository.MovieInfoRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class MoviesInfoControllerIntgTest {

  @Autowired
  private MovieInfoRepository movieInfoRepository;

  @Autowired
  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    List<MovieInfo> movieInfos = List.of(
        new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Caine"),
            LocalDate.parse("2005-06-15")),
        new MovieInfo(null, "The Dark Knight", 2005, List.of("Christian Bale", "HeathLedger"),
                LocalDate.parse("2008-07-18")),
        new MovieInfo("123", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"),
            LocalDate.parse("2012-07-20")));

    movieInfoRepository.saveAll(movieInfos).blockLast();
  }

  @AfterEach
  void tearDown() {
    movieInfoRepository.deleteAll().block();
  }

  @Test
  void testAddMovieInfo() {
    // given

    MovieInfo movieInfo = new MovieInfo(null, "Batman Begins1", 2005,
        List.of("Christian Bale", "Michael Caine"), LocalDate.parse("2005-06-15"));
    // when
    webTestClient.post().uri("/v1/movieinfos").bodyValue(movieInfo).exchange().expectStatus()
        .isCreated().expectBody(MovieInfo.class).consumeWith(movieInfoEntityExchangeResult -> {
          MovieInfo savedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
          assert savedMovieInfo != null;
          assert savedMovieInfo.getMovieInfoId() != null;
        });

    // then
  }

  @Test
  void testGetAllMovieInfos() {
    // given


    // when
    webTestClient.get().uri("/v1/movieinfos").exchange().expectStatus().is2xxSuccessful()
        .expectBodyList(MovieInfo.class).hasSize(3).consumeWith(listEntityExchangeResult -> {
          List<MovieInfo> movieInfos = listEntityExchangeResult.getResponseBody();
          assert movieInfos != null;
          MovieInfo movieInfo = movieInfos.get(0);
          assert movieInfo.getName().equals("Batman Begins");
          assert movieInfo.getYear() == 2005;
          assert movieInfo.getCast().containsAll(List.of("Christian Bale", "Michael Caine"));
          assert movieInfo.getReleaseDate().equals(LocalDate.parse("2005-06-15"));
        });

    // then
  }

  @Test
  void testGetMovieInfoById() {
    // given

    // when
    webTestClient.get().uri("/v1/movieinfos/" + "{id}", "123").exchange().expectStatus()
        .is2xxSuccessful().expectBody(MovieInfo.class)
        .consumeWith(movieInfoEntityExchangeResult -> {
          MovieInfo movieInfo = movieInfoEntityExchangeResult.getResponseBody();
          assert movieInfo != null;
          assert movieInfo.getMovieInfoId().equals("123");
        });

    // then
  }

  @Test
  void testGetMovieInfoById_notFound() {
    // given

    // when
    webTestClient.get().uri("/v1/movieinfos/" + "{id}", "def").exchange().expectStatus()
        .isNotFound();
    // then
  }

  @Test
  void testGetMovieInfoById_JsonPath() {
    // given

    // when
    webTestClient.get().uri("/v1/movieinfos/" + "{id}", "123").exchange().expectStatus()
        .is2xxSuccessful().expectBody().jsonPath("$.name").isEqualTo("Dark Knight Rises");

    // then
  }

  @Test
  void testUpdateMovieInfo() {
    // given
    MovieInfo movieInfo = new MovieInfo(null, "Batman Begins1", 2005,
        List.of("Christian Bale", "Michael Caine"), LocalDate.parse("2005-06-15"));
    // when
    webTestClient.put().uri("/v1/movieinfos/" + "{id}", "123").bodyValue(movieInfo).exchange()
        .expectStatus().is2xxSuccessful().expectBody(MovieInfo.class)
        .consumeWith(movieInfoEntityExchangeResult -> {
          MovieInfo updatedMovieInfo = movieInfoEntityExchangeResult.getResponseBody();
          assert updatedMovieInfo != null;
          assert updatedMovieInfo.getMovieInfoId().equals("123");
          assert updatedMovieInfo.getName().equals("Batman Begins1");
        });

    // then
  }


  @Test
  void testUpdateMovieInfo_notFound() {
    // given
    MovieInfo movieInfo = new MovieInfo(null, "Batman Begins1", 2005,
        List.of("Christian Bale", "Michael Caine"), LocalDate.parse("2005-06-15"));

    String id = "def";
    // when
    webTestClient.put().uri("/v1/movieinfos/" + "{id}", id).bodyValue(movieInfo).exchange()
        .expectStatus().isNotFound();
    // then
  }

  @Test
  void testDeleteMovieInfo() {
    // given

    // when
    webTestClient.delete().uri("/v1/movieinfos/" + "{id}", "123").exchange().expectStatus()
        .isNoContent();

    // Check that we now have only 2 objects
    webTestClient.get().uri("/v1/movieinfos").exchange().expectStatus().isOk()
        .expectBodyList(MovieInfo.class).hasSize(2);

    // then
  }

  @Test
  void testFindByYear() {
    // given

    // when
    int year = 2005;
    webTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/v1/movieinfos").queryParam("year", year).build())
        .exchange().expectStatus().isOk().expectBodyList(MovieInfo.class).hasSize(2);

    // then
  }

}
