package com.itprotopics.cursos.reactive.movies_info_service.controller;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoControllerTest {
  @Test
  void testFlux() {

    FluxAndMonoController fluxAndMonoController = new FluxAndMonoController();

    Flux<Integer> flux = fluxAndMonoController.flux();

    StepVerifier.create(flux).expectNext(1, 2, 3, 4).verifyComplete();
  }

}
