package com.itprotopics.cursos.reactive.movies_service.util;

import java.time.Duration;

import com.itprotopics.cursos.reactive.movies_service.exception.MoviesInfoServerException;

import reactor.core.Exceptions;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

public class RetryUtil {

  public static RetryBackoffSpec getRetrySpec() {
    return Retry.fixedDelay(3, Duration.ofSeconds(1))
        .filter(ex -> ex instanceof MoviesInfoServerException)
        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure()));
  }

}