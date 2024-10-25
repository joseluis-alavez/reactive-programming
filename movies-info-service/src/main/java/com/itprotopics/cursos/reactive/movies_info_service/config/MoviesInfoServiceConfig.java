package com.itprotopics.cursos.reactive.movies_info_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.itprotopics.cursos.reactive.movies_info_service.domain.MovieInfo;

import reactor.core.publisher.Sinks;

@Configuration
public class MoviesInfoServiceConfig {

  @Bean
  public Sinks.Many<MovieInfo> movieInfoSinks() {
    return Sinks.many().replay().all();
  }

}