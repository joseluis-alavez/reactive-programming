package com.itprotopics.cursos.reactive.reactive_programming;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.itprotopics.cursos.reactive.reactive_programming.services.FluxAndMonoGenerartorService;

@SpringBootApplication
public class ReactiveProgrammingApplication {
  public static void main(String[] args) {
    FluxAndMonoGenerartorService service = new FluxAndMonoGenerartorService();

    System.out.println("Flux example:");
    service.namesFlux().subscribe(System.out::println);

    System.out.println("\nMono example:");
    service.namesMono().subscribe(System.out::println);

    System.out.println("\nFlux with map example:");
    service.namesFluxMap().subscribe(System.out::println);

    // Add more examples as needed
  }

}
