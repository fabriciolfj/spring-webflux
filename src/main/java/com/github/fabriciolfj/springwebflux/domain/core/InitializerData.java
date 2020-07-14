package com.github.fabriciolfj.springwebflux.domain.core;

import com.github.fabriciolfj.springwebflux.domain.document.Item;
import com.github.fabriciolfj.springwebflux.domain.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@Profile("!test")
public class InitializerData implements CommandLineRunner {

    @Autowired
    private ItemRepository itemRepository;

    private List<Item> itemList = Arrays.asList(
            new Item(null, "Samsung Tv", 400.00),
            new Item(null, "Apple watch", 420.00),
            new Item(null, "beats headphones", 149.99)
    );

    @Override
    public void run(String... args) throws Exception {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemRepository::save)
                .thenMany(itemRepository.findAll())
                .subscribe(item -> log.info("Inserted : " + item));
    }
}
