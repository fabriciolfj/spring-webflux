package com.github.fabriciolfj.springwebflux.fluxandmongoplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void transformUsingMap() {
        Flux<String> fluxName = Flux.fromIterable(names)
                .map(s -> s.toUpperCase());

        StepVerifier.create(fluxName)
                .expectNext("ADAM", "ANNA", "JACK", "JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_filter() {
        Flux<String> fluxName = Flux.fromIterable(names)
                .filter(n -> n.length() > 4)
                .map(s -> s.toUpperCase());

        StepVerifier.create(fluxName)
                .expectNext("JENNY")
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F"))
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));
                })
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void transformUsingFlatMap_usingparallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")) //Flux<String>
                .window(2)//Flux<Flux<String>> -> (A,B) (C,D) (E,F)
                .flatMap(s ->
                        s.map(this::convertToList).subscribeOn(Schedulers.parallel()))//Flux<List<String>>
                .flatMap(p -> Flux.fromIterable(p)) //Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    /*
    * Para manter a ordem dos eventos, podemos utilizar o concatmap ou flatMapSequential, este ultimo é mais performático.
    * */
    @Test
    public void transformUsingFlatMap_usingparallel_maintain_order() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A", "B", "C", "D", "E", "F")) //Flux<String>
                .window(2)//Flux<Flux<String>> -> (A,B) (C,D) (E,F)
                .flatMapSequential(s ->
                        s.map(this::convertToList).subscribeOn(Schedulers.parallel()))//Flux<List<String>>
                .flatMap(p -> Flux.fromIterable(p)) //Flux<String>
                .log();

        StepVerifier.create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Arrays.asList(s, "newValue");
    }
}
