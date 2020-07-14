package com.github.fabriciolfj.springwebflux.api.handler;

import com.github.fabriciolfj.springwebflux.domain.document.Item;
import com.github.fabriciolfj.springwebflux.domain.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ItemHandlerFunction {

    @Autowired
    private ItemService service;

    public Mono<ServerResponse> findAll(final ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(service.findAll(), Item.class);
    }
}
