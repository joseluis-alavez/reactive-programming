package com.itprotopics.cursos.reactive.reactive_programming.services;

import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoGenerartorServiceTest {

  private FluxAndMonoGenerartorService fluxAndMonoGeneratorService =
      new FluxAndMonoGenerartorService();

  @Test
  void namesFlux() {

    Flux<String> names = fluxAndMonoGeneratorService.namesFlux();

    StepVerifier.create(names)
        // .expectNext("alex", "ben", "chloe")
        .expectNextCount(3).verifyComplete();

    System.out.println("test finished");
  }

  @Test
  void namesFluxMap() {
    Flux<String> names = fluxAndMonoGeneratorService.namesFluxMap();
    StepVerifier.create(names).expectNext("ALEX", "BEN", "CHLOE").verifyComplete();


  }

  @Test
  void namesFluxMap_inmutablility() {

    Flux<String> names = fluxAndMonoGeneratorService.namesFluxMap_inmutablility();
    StepVerifier.create(names).expectNext("alex", "ben", "chloe").verifyComplete();
  }

  @Test
  void namesFluxMapFilter() {
    Flux<String> names = fluxAndMonoGeneratorService.namesFluxMapFilter(3);
    StepVerifier.create(names).expectNext("4-ALEX", "5-CHLOE").verifyComplete();

    names.blockLast();


  }

  @Test
  void namesFluxFlatMap() {
    Flux<String> names = fluxAndMonoGeneratorService.namesFluxFlatMap(3);
    StepVerifier.create(names).expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
  }

  @Test
  void namesFluxFlatMapWithDe() {
    Flux<String> names = fluxAndMonoGeneratorService.namesFluxFlatMapWithDelay(3);
    StepVerifier.create(names).expectNextCount(9).verifyComplete();
  }

  @Test
  void namesFluxConcatMapWithDelay() {
    Flux<String> names = fluxAndMonoGeneratorService.namesFluxConcatMapWithDelay(3);
    StepVerifier.create(names).expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
  }

  @Test
  void namesMonoFlatMap() {
    Mono<List<String>> names = fluxAndMonoGeneratorService.namesMonoFlatMap();
    StepVerifier.create(names).expectNext(List.of("A", "L", "E", "X")).verifyComplete();
  }

  @Test
  void namesMonoFlatMapMany() {
    Flux<String> names = fluxAndMonoGeneratorService.namesMonoFlatMapMany();
    StepVerifier.create(names).expectNext("A", "L", "E", "X").verifyComplete();
  }

  @Test
  void namesFluxFlatMap_transform() {
    Flux<String> names = fluxAndMonoGeneratorService.namesFluxFlatMap_transform(3);
    StepVerifier.create(names).expectNext("A", "L", "E", "X", "C", "H", "L", "O", "E")
        .verifyComplete();
  }

  @Test
  void namesFluxFlatMap_transform_1() {
    Flux<String> names = fluxAndMonoGeneratorService.namesFluxFlatMap_transform(6);
    StepVerifier.create(names).expectNext("default").verifyComplete();
  }

  @Test
  void namesFluxFlatMap_transform_switchIfEmpty() {
    Flux<String> names = fluxAndMonoGeneratorService.namesFluxFlatMap_transform_switchIfEmpty(6);
    StepVerifier.create(names).expectNext("D", "E", "F", "A", "U", "L", "T").verifyComplete();
  }

  @Test
  void explore_concat() {
    Flux<String> names = fluxAndMonoGeneratorService.explore_concat();
    StepVerifier.create(names).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
  }

  @Test
  void explore_concatWith() {
    Flux<String> names = fluxAndMonoGeneratorService.explore_concatWith();
    StepVerifier.create(names).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
  }

  @Test
  void explore_concateWith_monno() {
    Flux<String> names = fluxAndMonoGeneratorService.explore_concateWith_monno();
    StepVerifier.create(names).expectNext("A", "B").verifyComplete();
  }

  @Test
  void explore_merge() {
    Flux<String> names = fluxAndMonoGeneratorService.explore_merge();
    StepVerifier.create(names).expectNext("A", "D", "B", "E", "C", "F").verifyComplete();
  }

  @Test
  void explore_mergeWith() {
    Flux<String> names = fluxAndMonoGeneratorService.explore_mergeWith();
    StepVerifier.create(names).expectNext("A", "D", "B", "E", "C", "F").verifyComplete();
  }

  @Test
  void explore_mergeSequential() {
    Flux<String> names = fluxAndMonoGeneratorService.explore_mergeSequential();
    StepVerifier.create(names).expectNext("A", "B", "C", "D", "E", "F").verifyComplete();
  }

  @Test
  void explore_zip() {
    Flux<String> names = fluxAndMonoGeneratorService.explore_zip();
    StepVerifier.create(names).expectNext("AD", "BE", "CF").verifyComplete();
  }

  @Test
  void explore_zip2() {
    Flux<String> names = fluxAndMonoGeneratorService.explore_zip2();
    StepVerifier.create(names).expectNext("AD14", "BE25", "CF36").verifyComplete();
  }

  @Test
  void explore_zipWith() {
    Flux<String> names = fluxAndMonoGeneratorService.explore_zipWith();
    StepVerifier.create(names).expectNext("AD", "BE", "CF").verifyComplete();
  }

  @Test
  void explore_zipWith_mono() {
    Mono<String> names = fluxAndMonoGeneratorService.explore_zipWith_mono();
    StepVerifier.create(names).expectNext("AB").verifyComplete();
  }

}
