package com.itprotopics.cursos.reactive.movies_review_service.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.itprotopics.cursos.reactive.movies_review_service.handler.ReviewHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;

@Configuration
public class ReviewRouter {

  @Bean
  public RouterFunction<ServerResponse> reviewsRoute(ReviewHandler reviewHandler) {
    return RouterFunctions.route()
        .nest(path("/v1/reviews"),
            builder -> builder.POST("", request -> reviewHandler.addReview(request))
                .GET("", request -> reviewHandler.getReviews(request))
                .GET("/{id}", request -> reviewHandler.getReviewById(request))
                .PUT("/{id}", request -> reviewHandler.updateReview(request))
                .DELETE("/{id}", request -> reviewHandler.deleteReview(request)))
        .GET("/helloworld", (request -> ServerResponse.ok().bodyValue("Hello World"))).build();
  }
}
