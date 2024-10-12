package com.itprotopics.cursos.reactive.movies_info_service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import com.itprotopics.cursos.reactive.movies_info_service.domain.MovieInfo;

public interface MovieInfoRepository extends ReactiveMongoRepository<MovieInfo, String> {

}
