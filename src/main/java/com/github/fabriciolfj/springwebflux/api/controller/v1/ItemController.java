package com.github.fabriciolfj.springwebflux.api.controller.v1;

import com.github.fabriciolfj.springwebflux.domain.document.Item;
import com.github.fabriciolfj.springwebflux.domain.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(final RuntimeException ex) {
        log.error("Exception caught in handleRuntimeException: {}", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @GetMapping("/v1/items")
    public Flux<Item> findAll() {
        return itemService.findAll();
    }

    @GetMapping("/v1/items/{id}")
    public Mono<ResponseEntity<Item>> findById(@PathVariable("id") final String id) {
        return itemService.findById(id)
                .map(item -> ResponseEntity.ok().body(item))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/v1/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> create(@RequestBody final Item item) {
        return itemService.create(item);
    }

    @DeleteMapping("/v1/items/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable final String id) {
        return itemService.delete(id);
    }

    @PutMapping("/v1/items/{id}")
    public Mono<ResponseEntity<Item>> update(@PathVariable final String id, @RequestBody final Item item) {
        return itemService.update(id, item)
                .map(value -> ResponseEntity.ok().body(item))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("/v1/error")
    public Flux<Item> getAllItemsError() {
        return itemService.findAll()
                .concatWith(Mono.error(new RuntimeException("Fail ocurred.")));
    }
}
