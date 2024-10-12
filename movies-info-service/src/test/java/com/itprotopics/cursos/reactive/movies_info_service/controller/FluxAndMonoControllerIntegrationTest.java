package com.itprotopics.cursos.reactive.movies_info_service.controller;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@WebFluxTest(FluxAndMonoController.class)
@AutoConfigureWebTestClient
public class FluxAndMonoControllerIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void testFlux() {
    webTestClient.get().uri("/flux").exchange().expectStatus().isOk().expectBodyList(Integer.class)
        .hasSize(4);
  }

  @Test
  void testFlux_approach2() {
    Flux<Integer> fluxResult = webTestClient.get().uri("/flux").exchange().expectStatus().isOk()
        .returnResult(Integer.class).getResponseBody();

    StepVerifier.create(fluxResult).expectNext(1, 2, 3, 4).verifyComplete();
  }

  @Test
  void testFlux_approach3() {
    webTestClient.get().uri("/flux").exchange().expectStatus().isOk().expectBodyList(Integer.class)
        .consumeWith(response -> {
          List<Integer> result = response.getResponseBody();
          assert (Objects.requireNonNull(result)).size() == 4;
        });
  }

  @Test
  void testMono() {
    webTestClient.get().uri("/mono").exchange().expectStatus().isOk().expectBody(String.class)
        .consumeWith(response -> {
          String result = response.getResponseBody();
          assert Objects.nonNull(result);

          assertEquals("Hello World", result);
        });
  }


  @Test
  void testStream() {
    Flux<Long> fluxResult = webTestClient.get().uri("/stream").exchange().expectStatus().isOk()
        .returnResult(Long.class).getResponseBody();

    StepVerifier.create(fluxResult).expectNext(0L, 1L, 2L, 3L).thenCancel().verify();
  }
}
