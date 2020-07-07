package com.github.fabriciolfj.springwebflux.fluxandmongoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

public class FluxAndMonoTest {

    @Test
    public void fluxTest() {
        Flux<String> stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
                //.concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("After error"))
                .log();

        stringFlux.map(s -> s.concat("flux"))
                .subscribe(s -> System.out.println(s),
                        e -> System.err.println("Exception is " + e),
                        () -> System.out.println("Complete Stream"));

    }
}
