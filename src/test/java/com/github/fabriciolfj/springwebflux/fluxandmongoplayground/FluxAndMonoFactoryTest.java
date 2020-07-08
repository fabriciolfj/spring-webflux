package com.github.fabriciolfj.springwebflux.fluxandmongoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void fluxUsingIterable() {
        Flux<String> fluxNames = Flux.fromIterable(names);

        StepVerifier.create(fluxNames)
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingArray() {
        String[] names = new String[] {"adam", "anna", "jack", "jenny"};

        Flux<String> fluxNames = Flux.fromArray(names);

        StepVerifier.create(fluxNames)
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void fluxUsingStream() {
        Flux<String> fluxNames = Flux.fromStream(names.stream());
        StepVerifier.create(fluxNames)
                .expectNext("adam", "anna", "jack", "jenny")
                .verifyComplete();
    }

    @Test
    public void monoJustOrEmpty() {
        Mono<String> name = Mono.justOrEmpty(null); //Mono.empty()
        StepVerifier.create(name.log())
                .verifyComplete();
    }

    @Test
    public void monoUsingSupplier() {
        Supplier<String> name = () -> "adam";
        Mono<String> monoName = Mono.fromSupplier(name);
        System.out.println(name.get());

        StepVerifier.create(monoName.log())
                .expectNext("adam")
                .verifyComplete();
    }

    @Test
    public void fluxRange() {
        Flux<Integer> range = Flux.range(1, 5).log();

        StepVerifier.create(range)
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }

}
