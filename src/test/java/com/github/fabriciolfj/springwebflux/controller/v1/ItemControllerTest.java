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

import java.util.Arrays;
import java.util.List;

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
}
