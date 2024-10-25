package com.itprotopics.cursos.reactive.movies_review_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.itprotopics.cursos.reactive.movies_review_service.domain.Review;

import reactor.core.publisher.Sinks;

@Configuration
public class MoviesReviewConfig {

  @Bean
  public Sinks.Many<Review> reviewSinks() {
    return Sinks.many().replay().all();
  }
}
