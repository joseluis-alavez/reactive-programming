package com.itprotopics.cursos.reactive.reactive_programming.services;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxAndMonoGenerartorService {

  public Flux<String> namesFlux() {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).log();
  }

  public Mono<String> namesMono() {
    return Mono.just("alex").log();
  }

  public Flux<String> namesFluxMap() {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).map(String::toUpperCase).log();
  }


  public Flux<String> namesFluxMap_inmutablility() {
    Flux<String> namesFlux = Flux.fromIterable(List.of("alex", "ben", "chloe"));

    namesFlux.map(String::toUpperCase).log();

    return namesFlux;
  }

  public Flux<String> namesFluxMapFilter(Integer stringLength) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).filter(s -> s.length() > stringLength)
        .map(name -> {
          return name.length() + "-" + name.toUpperCase();
        }).log();
  }

  public Flux<String> namesFluxFlatMap(Integer stringLength) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).filter(s -> s.length() > stringLength)
        .map(String::toUpperCase).flatMap(this::splitName).log();
  }

  public Flux<String> namesFluxFlatMapWithDelay(Integer stringLength) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).filter(s -> s.length() > stringLength)
        .map(String::toUpperCase).flatMap(this::splitNameWithDelay).log();
  }

  public Flux<String> namesFluxConcatMapWithDelay(Integer stringLength) {
    return Flux.fromIterable(List.of("alex", "ben", "chloe")).filter(s -> s.length() > stringLength)
        .map(String::toUpperCase).concatMap(this::splitNameWithDelay).log();
  }

  /**
   * Demonstrates the use of flatMap with a Mono to transform a single string into a list of
   * characters.
   *
   * @return A Mono that emits a List of individual characters from the uppercase version of "alex".
   *
   *         This method performs the following operations: 1. Creates a Mono with the initial value
   *         "alex". 2. Transforms "alex" to uppercase using map(). 3. Uses flatMap() to apply the
   *         splitNameMono() method, which likely splits the string into a list of characters. 4.
   *         Logs the operations for debugging purposes.
   */
  public Mono<List<String>> namesMonoFlatMap() {
    return Mono.just("alex").map(String::toUpperCase).flatMap(this::splitNameMono).log();
  }

  public Flux<String> namesMonoFlatMapMany() {
    return Mono.just("alex").map(String::toUpperCase).flatMapMany(this::splitName).log();
  }

  public Flux<String> namesFluxFlatMap_transform(Integer stringLength) {

    Function<Flux<String>, Flux<String>> filterMap =
        name -> name.map(String::toUpperCase).filter(s -> s.length() > stringLength);

    return Flux.fromIterable(List.of("alex", "ben", "chloe")).transform(filterMap)
        .flatMap(this::splitName).defaultIfEmpty("default").log();
  }

  public Flux<String> namesFluxFlatMap_transform_switchIfEmpty(Integer stringLength) {

    Function<Flux<String>, Flux<String>> filterMap = name -> name.map(String::toUpperCase)
        .filter(s -> s.length() > stringLength).flatMap(this::splitName);

    var defaultFlux = Flux.just("default").transform(filterMap);

    return Flux.fromIterable(List.of("alex", "ben", "chloe")).transform(filterMap)
        .switchIfEmpty(defaultFlux).log();
  }

  public Flux<String> explore_concat() {
    Flux<String> abcFlux = Flux.just("A", "B", "C");
    Flux<String> defFlux = Flux.just("D", "E", "F");
    return Flux.concat(abcFlux, defFlux).log();
  }

  public Flux<String> explore_concatWith() {
    Flux<String> abcFlux = Flux.just("A", "B", "C");
    Flux<String> defFlux = Flux.just("D", "E", "F");
    return abcFlux.concatWith(defFlux).log();
  }

  public Flux<String> explore_concateWith_monno() {
    Mono<String> aMono = Mono.just("A");
    Mono<String> bMono = Mono.just("B");
    return aMono.concatWith(bMono).log();
  }

  public Flux<String> explore_merge() {

    Flux<String> abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));

    Flux<String> defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

    return Flux.merge(abcFlux, defFlux).log();
  }

  public Flux<String> explore_mergeWith() {

    Flux<String> abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));

    Flux<String> defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

    return abcFlux.mergeWith(defFlux).log();
  }

  public Flux<String> explore_mergeSequential() {

    Flux<String> abcFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(100));

    Flux<String> defFlux = Flux.just("D", "E", "F").delayElements(Duration.ofMillis(125));

    return Flux.mergeSequential(abcFlux, defFlux).log();
  }

  public Flux<String> explore_zip() {

    Flux<String> abcFlux = Flux.just("A", "B", "C");
    Flux<String> defFlux = Flux.just("D", "E", "F");


    return Flux.zip(abcFlux, defFlux, (first, second) -> first + second).log();
  }

  public Flux<String> explore_zip2() {

    Flux<String> abcFlux = Flux.just("A", "B", "C");
    Flux<String> defFlux = Flux.just("D", "E", "F");
    Flux<String> flux123 = Flux.just("1", "2", "3");
    Flux<String> flux456 = Flux.just("4", "5", "6");

    return Flux.zip(abcFlux, defFlux, flux123, flux456)
        .map(tuple -> tuple.getT1() + tuple.getT2() + tuple.getT3() + tuple.getT4()).log();
  }

  public Flux<String> explore_zipWith() {

    Flux<String> abcFlux = Flux.just("A", "B", "C");
    Flux<String> defFlux = Flux.just("D", "E", "F");

    return abcFlux.zipWith(defFlux, (first, second) -> first + second).log();
  }

  public Mono<String> explore_zipWith_mono() {

    Mono<String> aMono = Mono.just("A");
    Mono<String> bMono = Mono.just("B");

    return aMono.zipWith(bMono).map(t2 -> t2.getT1() + t2.getT2()).log();
  }

  private Mono<List<String>> splitNameMono(String name) {
    String[] splitName = name.split("");
    return Mono.just(List.of(splitName)).log();
  }

  private Flux<String> splitName(String name) {
    String[] splitName = name.split("");
    return Flux.fromArray(splitName).log();
  }

  private Flux<String> splitNameWithDelay(String name) {
    String[] splitName = name.split("");
    int delay = new Random().nextInt(1000);
    return Flux.fromArray(splitName).delayElements(Duration.ofMillis(delay));
  }



}
