package com.github.fabriciolfj.springwebflux.domain.service;

import com.github.fabriciolfj.springwebflux.domain.document.Item;
import com.github.fabriciolfj.springwebflux.domain.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public Flux<Item> findAll() {
        return itemRepository.findAll();
    }
}
