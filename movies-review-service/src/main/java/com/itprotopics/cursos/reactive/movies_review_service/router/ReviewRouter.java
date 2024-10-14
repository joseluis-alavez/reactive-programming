package com.itprotopics.cursos.reactive.movies_review_service.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.itprotopics.cursos.reactive.movies_review_service.handler.ReviewHandler;

@Configuration
public class ReviewRouter {

  @Bean
  public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler reviewHandler) {
    return RouterFunctions.route()
        .GET("/v1/helloworld", (request -> ServerResponse.ok().bodyValue("Hello World")))
        .POST("/v1/reviews", request -> reviewHandler.addReview(request)).build();
  }
}
