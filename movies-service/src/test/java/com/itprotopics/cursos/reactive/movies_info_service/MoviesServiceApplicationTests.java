package com.itprotopics.cursos.reactive.movies_info_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.itprotopics.cursos.reactive.movies_service.MoviesServiceApplication;

@SpringBootTest
@ContextConfiguration(classes = { MoviesServiceApplication.class })
class MoviesServiceApplicationTests {

  @Test
  void contextLoads() {
  }

}
