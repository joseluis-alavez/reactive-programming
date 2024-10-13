package com.itprotopics.cursos.reactive.movies_info_service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.itprotopics.cursos.reactive.movies_info_service.domain.MovieInfo;
import reactor.core.publisher.Flux;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

  Flux<MovieInfo> findByYear(Integer year);

  Flux<MovieInfo> findByName(String name);
}

