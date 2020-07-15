package com.github.fabriciolfj.springwebflux.controller.v1;

import com.github.fabriciolfj.springwebflux.domain.document.Item;
import com.github.fabriciolfj.springwebflux.domain.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest
@DirtiesContext
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ItemRepository itemRepository;

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
        webTestClient.get().uri("/v1/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(3);
    }

    @Test
    public void getAllItems_approach2() {
        webTestClient.get().uri("/v1/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Item.class)
                .hasSize(3)
                .consumeWith(response -> {
                    List<Item> items = response.getResponseBody();
                    items.stream().forEach(item -> assertTrue(item.getId() != null));
                });
    }

    @Test
    public void getAllItems_approach3() {
        Flux<Item> items = webTestClient.get().uri("/v1/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(items.log("Value: "))
                .expectSubscription()
                .expectNextCount(3);
    }

    @Test
    public void getOneItem() {
        webTestClient.get().uri("/v1/items/{id}", "ABC")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 149.99);
    }

    @Test
    public void getOneItem_notfound() {
        webTestClient.get().uri("/v1/items/{id}", "ABC2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItem() {
        var item = new Item(null, "Iphone x", 999.99);
        webTestClient.post().uri("/v1/items")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.justOrEmpty(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Iphone x")
                .jsonPath("$.price").isEqualTo(999.99);
    }

    @Test
    public void deleteItem() {
        webTestClient.delete().uri("/v1/items/{id}", "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);
    }

    @Test
    public void update() {
        var newPrice = 179.88;
        var item = new Item(null, "Apple watch", newPrice);

        webTestClient.put().uri("/v1/items/{id}", "ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.justOrEmpty(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo(newPrice)
                .jsonPath("$.description").isEqualTo(item.getDescription());
    }

    @Test
    public void update_error() {
        var newPrice = 179.88;
        var item = new Item(null, "Apple watch", newPrice);

        webTestClient.put().uri("/v1/items/{id}", "ABC2")
                .body(Mono.justOrEmpty(item), Item.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void getOneItemV2() {
        webTestClient.get().uri("/v2/items/{id}", "ABC")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 149.99);
    }

    @Test
    public void getOneItem_notfoundV2() {
        webTestClient.get().uri("/v2/items/{id}", "ABC2")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItemV2() {
        var item = new Item(null, "Iphone x", 999.99);
        webTestClient.post().uri("/v2/items")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.justOrEmpty(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Iphone x")
                .jsonPath("$.price").isEqualTo(999.99);
    }

    @Test
    public void deleteItemV2() {
        webTestClient.delete().uri("/v2/items/{id}", "ABC")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateV2() {
        var newPrice = 179.88;
        var item = new Item(null, "Apple watch", newPrice);

        webTestClient.put().uri("/v2/items/{id}", "ABC")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.justOrEmpty(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo(newPrice)
                .jsonPath("$.description").isEqualTo(item.getDescription());
    }

    @Test
    public void update_errorV2() {
        var newPrice = 179.88;
        var item = new Item(null, "Apple watch", newPrice);

        webTestClient.put().uri("/v2/items/{id}", "ABC2")
                .body(Mono.justOrEmpty(item), Item.class)
                .exchange()
                .expectStatus().isBadRequest();
    }
}
