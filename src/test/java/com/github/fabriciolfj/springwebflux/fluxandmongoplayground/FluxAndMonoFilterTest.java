package com.github.fabriciolfj.springwebflux.fluxandmongoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void fluxTestStart() {
        Flux<String> fluxNames = Flux.fromIterable(names).filter(s -> s.startsWith("a"))
                .log();

        StepVerifier.create(fluxNames)
                .expectNext("adam", "anna")
                .verifyComplete();
    }

    @Test
    public void fluxTestLenght() {
        Flux<String> fluxNames = Flux.fromIterable(names).filter(s -> s.length() > 4)
                .log();

        StepVerifier.create(fluxNames)
                .expectNext("jenny")
                .verifyComplete();
    }
}
