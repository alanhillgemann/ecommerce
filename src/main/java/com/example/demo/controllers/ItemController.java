package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/item")
@RestController
public class ItemController {

    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        return ResponseEntity.ok(itemRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Optional<Item> item = itemRepository.findById(id);
        if (!item.isPresent()) {
            log.error("GET /api/item/{} | Not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("GET /api/item/{}", id);
        return ResponseEntity.of(item);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
        List<Item> items = itemRepository.findByName(name);
        if (items.isEmpty()) {
            log.error("GET /api/item/{} | Not found", name);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("GET /api/item/{}", name);
        return ResponseEntity.ok(items);
    }
}
