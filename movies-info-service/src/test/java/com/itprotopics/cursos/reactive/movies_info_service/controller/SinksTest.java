package com.itprotopics.cursos.reactive.movies_info_service.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
@SpringBootTest
public class SinksTest {
  @Test
  public void testSinks() {
    // given
    Sinks.Many<Integer> replaySink = Sinks.many().replay().all();

    // when
    replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
    replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);
    // then
    Flux<Integer> integerFlux = replaySink.asFlux();
    integerFlux.subscribe((i) -> {
      log.info("Received 1:  " + i);
    });

    Flux<Integer> integerFlux1 = replaySink.asFlux();
    integerFlux1.subscribe((i) -> {
      log.info("Received 2:  " + i);
    });

    replaySink.tryEmitNext(3);

    // Force the test to wait for the subscriptions to complete
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSinks_multicast() {
    Sinks.Many<Integer> multicastSink = Sinks.many().multicast().onBackpressureBuffer();

    // when
    multicastSink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
    multicastSink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

    Flux<Integer> integerFlux = multicastSink.asFlux();
    integerFlux.subscribe((i) -> {
      log.info("Received 1:  " + i);
    });

    Flux<Integer> integerFlux1 = multicastSink.asFlux();
    integerFlux1.subscribe((i) -> {
      log.info("Received 2:  " + i);
    });

    multicastSink.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);

    // then
  }

  @Test
  public void testSinks_unicast() {
    Sinks.Many<Integer> unicastSink = Sinks.many().unicast().onBackpressureBuffer();

    // when
    unicastSink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST);
    unicastSink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST);

    Flux<Integer> integerFlux = unicastSink.asFlux();
    integerFlux.subscribe((i) -> {
      log.info("Received 1:  " + i);
    });

    // Flux<Integer> integerFlux1 = unicastSink.asFlux();
    // integerFlux1.subscribe((i) -> {
    // log.info("Received 2: " + i);
    // });

    unicastSink.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST);

    // then
  }
}
