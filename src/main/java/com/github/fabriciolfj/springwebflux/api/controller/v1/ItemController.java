package com.github.fabriciolfj.springwebflux.api.controller.v1;

import com.github.fabriciolfj.springwebflux.domain.document.Item;
import com.github.fabriciolfj.springwebflux.domain.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/v1/items")
    public Flux<Item> findAll() {
        return itemService.findAll();
    }
}
