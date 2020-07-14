package com.github.fabriciolfj.springwebflux.domain.service;

import com.github.fabriciolfj.springwebflux.domain.document.Item;
import com.github.fabriciolfj.springwebflux.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Flux<Item> findAll() {
        return itemRepository.findAll();
    }

    public Mono<Item> findById(final String id) {
        return itemRepository.findById(id);
    }

    public Mono<Item> create(final Item item) {
        return itemRepository.save(item);
    }

    public Mono<Void> delete(final String id) {
        return itemRepository.deleteById(id);
    }

    public Mono<Item> update(String id, Item item) {
        return itemRepository.findById(id)
                .flatMap(current -> {
                    current.setPrice(item.getPrice());
                    current.setDescription(item.getDescription());
                    return itemRepository.save(current);
                });
    }
}
