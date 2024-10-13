package com.itprotopics.cursos.reactive.movies_info_service.controller;

import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.itprotopics.cursos.reactive.movies_info_service.domain.MovieInfo;
import com.itprotopics.cursos.reactive.movies_info_service.service.MoviesInfoService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(MoviesInfoController.class)
@AutoConfigureWebTestClient
public class MoviesInfoControllerUnitTest {


  @Autowired
  private WebTestClient webTestClient;

  @MockBean
  private MoviesInfoService moviesInfoService;

  static String MOVIE_INFO_URL = "/v1/movieinfos";

  @Test
  void getAllMoviesInfo() {

    List<MovieInfo> movieInfos = List.of(
        new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Caine"),
            LocalDate.parse("2005-06-15")),
        new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale", "HeathLedger"),
            LocalDate.parse("2008-07-18")),
        new MovieInfo("123", "Dark Knight Rises", 2012, List.of("Christian Bale", "Tom Hardy"),
            LocalDate.parse("2012-07-20")));

    when(moviesInfoService.getAllMovieInfos()).thenReturn(Flux.fromIterable(movieInfos));

    webTestClient.get().uri(MOVIE_INFO_URL).exchange().expectStatus().is2xxSuccessful()
        .expectBodyList(MovieInfo.class).hasSize(3).consumeWith(response -> {
          List<MovieInfo> responseBody = response.getResponseBody();
          assert responseBody != null;
          MovieInfo firstMovie = responseBody.get(0);
          assert firstMovie.getName().equals("Batman Begins");
          assert firstMovie.getYear() == 2005;
          assert firstMovie.getCast().equals(List.of("Christian Bale", "Michael Caine"));
          assert firstMovie.getReleaseDate().equals(LocalDate.parse("2005-06-15"));
        });

  }

  @Test
  void getMovieInfoById() {

    // given
    MovieInfo expectedMovieInfo = new MovieInfo("123", "Dark Knight Rises", 2012,
        List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"));

    // when
    when(moviesInfoService.getMovieInfoById("123")).thenReturn(Mono.just(expectedMovieInfo));

    webTestClient.get().uri(MOVIE_INFO_URL + "/{id}", "123").exchange().expectStatus()
        .is2xxSuccessful().expectBody(MovieInfo.class).isEqualTo(expectedMovieInfo);

  }

  @SuppressWarnings("null")
  @Test
  void addMovieInfo() {
    // given
    MovieInfo movieInfo = new MovieInfo("123-mock", "Batman Begins", 2005,
        List.of("Christian Bale", "Michael Caine"), LocalDate.parse("2005-06-15"));

    // when
    when(moviesInfoService.addMovieInfo(movieInfo)).thenReturn(Mono.just(movieInfo));

    webTestClient.post().uri(MOVIE_INFO_URL).bodyValue(movieInfo).exchange().expectStatus()
        .is2xxSuccessful().expectBody(MovieInfo.class).consumeWith(response -> {
          MovieInfo responseBody = response.getResponseBody();
          assert responseBody != null;
          assert responseBody.getMovieInfoId().equals("123-mock");
          assert responseBody.getName().equals("Batman Begins");
          assert responseBody.getYear() == 2005;
          assert responseBody.getCast().equals(List.of("Christian Bale", "Michael Caine"));
          assert responseBody.getReleaseDate().equals(LocalDate.parse("2005-06-15"));
        });
  }

  @Test
  void updateMovieInfo() {
    // given
    MovieInfo movieInfo = new MovieInfo("123-mock", "Batman Begins", 2005,
        List.of("Christian Bale", "Michael Caine"), LocalDate.parse("2005-06-15"));

    // when
    when(moviesInfoService.updateMovieInfo(movieInfo, "123-mock")).thenReturn(Mono.just(movieInfo));

    webTestClient.put().uri(MOVIE_INFO_URL + "/{id}", "123-mock").bodyValue(movieInfo).exchange()
        .expectStatus().is2xxSuccessful().expectBody(MovieInfo.class).isEqualTo(movieInfo);

  }

  @Test
  void deleteMovieInfo() {
    // when
    when(moviesInfoService.deleteMovieInfo("123-mock")).thenReturn(Mono.empty());

    webTestClient.delete().uri(MOVIE_INFO_URL + "/{id}", "123-mock").exchange().expectStatus()
        .isNoContent();
  }

  @SuppressWarnings("null")
  @Test
  void addMovieInfo_validation() {
    // given
    MovieInfo movieInfo =
        new MovieInfo(null, "", -2005, List.of(""), LocalDate.parse("2005-06-15"));


    webTestClient.post().uri(MOVIE_INFO_URL).bodyValue(movieInfo).exchange().expectStatus()
        .isBadRequest().expectBody(String.class).consumeWith(response -> {
          String responseBody = response.getResponseBody();
          String expectedErrorMessage =
              "Cast must be non empty, Name is required, Year must be greater than 0";
          assert responseBody.equals(expectedErrorMessage);
        });
  }

}
