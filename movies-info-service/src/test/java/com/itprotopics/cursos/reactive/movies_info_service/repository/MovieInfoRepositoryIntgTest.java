package com.itprotopics.cursos.reactive.movies_info_service.repository;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import com.itprotopics.cursos.reactive.movies_info_service.domain.MovieInfo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
@ActiveProfiles("test")
public class MovieInfoRepositoryIntgTest {

  @Autowired
  private MovieInfoRepository movieInfoRepository;

  @BeforeEach
  void setUp() {
    List<MovieInfo> movieinfos = List.of(
        new MovieInfo(null, "Batman Begins", 2005, List.of("Christian Bale", "Michael Cane"),
            LocalDate.parse("2005-06-15")),
        new MovieInfo(null, "The Dark Knight", 2008, List.of("Christian Bale", "HeathLedger"),
            LocalDate.parse("2008-07-18")),
        new MovieInfo("abc", "Batman Begins", 1989, List.of("Michael Keaton", "Jack Nicholson"),
            LocalDate.parse("1989-07-23")));

    movieInfoRepository.saveAll(movieinfos).blockLast();
  }

  @AfterEach
  void tearDown() {
    movieInfoRepository.deleteAll().block();
  }

  @Test
  void findAll() {
    // given

    // when
    Flux<MovieInfo> moviesInfoFlux = movieInfoRepository.findAll().log();
    // then
    StepVerifier.create(moviesInfoFlux).expectNextCount(3).verifyComplete();
  }

  @Test
  void findById() {
    // given

    // when
    Mono<MovieInfo> movieInfoMono = movieInfoRepository.findById("abc").log();
    // then
    StepVerifier.create(movieInfoMono).expectNextMatches(movieInfo -> {
      return movieInfo.getMovieInfoId().equals("abc") && movieInfo.getName().equals("Batman Begins")
          && movieInfo.getYear() == 1989
          && movieInfo.getCast().containsAll(List.of("Michael Keaton", "Jack Nicholson"))
          && movieInfo.getReleaseDate().equals(LocalDate.parse("1989-07-23"));
    }).verifyComplete();
  }

  @Test
  void saveMovieInfo() {
    // given
    MovieInfo movieInfo = new MovieInfo(null, "Batman Begins", 2005,
        List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));
    // when
    Mono<MovieInfo> savedMovieInfoMono = movieInfoRepository.save(movieInfo).log();
    // then
    StepVerifier.create(savedMovieInfoMono).expectNextMatches(savedMovie -> {
      return savedMovie.getMovieInfoId() != null && savedMovie.getName().equals("Batman Begins")
          && savedMovie.getYear() == 2005
          && savedMovie.getCast().containsAll(List.of("Christian Bale", "Michael Cane"))
          && savedMovie.getReleaseDate().equals(LocalDate.parse("2005-06-15"));
    }).verifyComplete();
  }

  @Test
  void updateMovieInfo() {

    // given
    MovieInfo movieInfo = movieInfoRepository.findById("abc").block();
    movieInfo.setYear(2020);
    // when
    Mono<MovieInfo> updatedMovieInfoMono = movieInfoRepository.save(movieInfo).log();
    // then
    StepVerifier.create(updatedMovieInfoMono).expectNextMatches(updatedMovieInfo -> {
      return updatedMovieInfo.getYear() == 2020;
    }).verifyComplete();

  }

  @Test
  void deleteMovieInfo() {
    // given

    // when
    movieInfoRepository.deleteById("abc").block();
    Mono<MovieInfo> deletedMovieInfoMono = movieInfoRepository.findById("abc").log();
    // then
    StepVerifier.create(deletedMovieInfoMono).expectNextCount(0).verifyComplete();
  }

}
