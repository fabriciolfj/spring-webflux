package com.github.fabriciolfj.springwebflux.fluxandmongoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> result = Flux.merge(flux1, flux2);

        StepVerifier.create(result.log())
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_withDelay() {
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));

        Flux<String> result = Flux.merge(flux1, flux2);

        StepVerifier.create(result.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat() {
        Flux<String> flux1 = Flux.just("A", "B", "C");
        Flux<String> flux2 = Flux.just("D", "E", "F");

        Flux<String> result = Flux.concat(flux1, flux2);

        StepVerifier.create(result.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingConcat_delay() {
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));;

        Flux<String> result = Flux.concat(flux1, flux2);

        StepVerifier.create(result.log())
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingZip() {
        Flux<String> flux1 = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1));;

        Flux<String> result = Flux.zip(flux1, flux2,  (t1, t2) -> {
            return t1.concat(t2); //AD, BE, CF
        }); // A,D : B,E : C:F

        StepVerifier.create(result.log())
                .expectNext("AD", "BE", "CF")
                .verifyComplete();
    }
}
