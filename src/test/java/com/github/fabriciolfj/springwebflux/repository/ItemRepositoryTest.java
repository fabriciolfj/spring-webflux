package com.github.fabriciolfj.springwebflux.repository;

import com.github.fabriciolfj.springwebflux.SpringWebfluxApplication;
import com.github.fabriciolfj.springwebflux.domain.document.Item;
import com.github.fabriciolfj.springwebflux.domain.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@Slf4j
@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
@DataMongoTest
@ActiveProfiles("test")
@AutoConfigureDataMongo
@ExtendWith(SpringExtension.class)
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    List<Item> itemList = Arrays.asList(
            new Item(null, "Samsung Tv", 400.00),
            new Item(null, "Apple watch", 420.00),
            new Item("ABC", "beats headphones", 149.99)
            );

    @BeforeEach
    public void setup() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemRepository::save)
                .doOnNext(item -> {
                    log.info("Inserted is : " + item.toString());
                }).blockLast(); // o ultimo evento vai esperar até que os anteriores ja tenham sidos inseridos
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(3L)
                .verifyComplete();
    }

    @Test
    public void getById() {
        StepVerifier.create(itemRepository.findById("ABC"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("beats headphones"))
                .verifyComplete();
    }

    @Test
    public void getDescription() {
        StepVerifier.create(itemRepository.findByDescription("Samsung Tv").log())
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Samsung Tv"))
                .verifyComplete();
    }

    @Test
    public void saveItem() {
        var item = new Item(null, "arroz", 9.99);
        Mono<Item> itemSave = itemRepository.save(item);

        StepVerifier.create(itemSave)
                .expectSubscription()
                .expectNextMatches(item1 -> item.getId() != null && item.getDescription().equals("arroz"))
                .verifyComplete();

    }

    @Test
    public void updateItem() {
        double newPrice = 520.00;
        Flux<Item> updateItem = itemRepository.findByDescription("Samsung Tv")
                .map(item -> {
                    item.setPrice(newPrice);
                    return item;
                })
                .flatMap(item -> itemRepository.save(item))
                .log();

        StepVerifier.create(updateItem)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == newPrice)
                .verifyComplete();
    }

    @Test
    public void deleteItemId() {
        Mono<Void> deleteItem = itemRepository.findById("ABC")
                .map(Item::getId)
                .flatMap(item -> itemRepository.deleteById(item));

        StepVerifier.create(deleteItem)
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(2L)
                .verifyComplete();
    }

    @Test
    public void deleteItemDescription() {
        StepVerifier.create(itemRepository.findByDescription("Samsung Tv")
                .flatMap(item -> {
                    return itemRepository.delete(item);
                }))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepository.findAll())
                .expectSubscription()
                .expectNextCount(2L)
                .verifyComplete();
    }
}

