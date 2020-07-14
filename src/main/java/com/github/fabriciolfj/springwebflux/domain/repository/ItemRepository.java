package com.github.fabriciolfj.springwebflux.domain.repository;

import com.github.fabriciolfj.springwebflux.domain.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {

    Flux<Item> findByDescription(final String description);
}
