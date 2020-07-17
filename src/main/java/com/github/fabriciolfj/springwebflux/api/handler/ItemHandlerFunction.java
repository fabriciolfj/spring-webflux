package com.github.fabriciolfj.springwebflux.api.handler;

import com.github.fabriciolfj.springwebflux.domain.document.Item;
import com.github.fabriciolfj.springwebflux.domain.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class ItemHandlerFunction {

    @Autowired
    private ItemService service;

    public Mono<ServerResponse> findAll(final ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Item.class);
    }

    public Mono<ServerResponse> getOneItem(final ServerRequest request) {
        var value = service.findById(request.pathVariable("id"));
        return value.flatMap(v ->
                ServerResponse.ok()
                        .body(BodyInserters.fromValue(v))).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createItem(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Item.class)
                .flatMap(item -> service.create(item))
                .flatMap(result -> ServerResponse.created(URI.create("v2/items/" + result.getId())).body(BodyInserters.fromValue(result)))
                .onErrorResume(e -> ServerResponse.badRequest().body(BodyInserters.fromValue(e.getMessage())));
    }

    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return service.delete(serverRequest.pathVariable("id"))
                .flatMap(result -> ServerResponse.ok().body(result, Void.class));
    }

    public Mono<ServerResponse> updateItem(ServerRequest serverRequest) {
        return service.findById(serverRequest.pathVariable("id"))
                .flatMap(item -> serverRequest.bodyToMono(Item.class)
                            .flatMap(body -> {
                                item.setDescription(body.getDescription());
                                item.setPrice(body.getPrice());
                                return service.create(item);
                            }))
                .flatMap(result -> ServerResponse.ok().body(BodyInserters.fromValue(result)))
                .switchIfEmpty(ServerResponse.badRequest().body(BodyInserters.fromValue("Product not found: " + serverRequest.pathVariable("id"))))
                .onErrorResume(e -> ServerResponse.badRequest().body(BodyInserters.fromValue(e.getMessage())));
    }

    public Mono<ServerResponse> error(ServerRequest serverRequest) {
        throw new RuntimeException("Error route");
    }
}
