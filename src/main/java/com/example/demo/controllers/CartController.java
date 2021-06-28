package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.stream.IntStream;

@RequestMapping("/api/cart")
@RestController
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addToCart")
    public ResponseEntity<Cart> addToCart(@RequestBody ModifyCartRequest request) {
        String username = request.getUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("POST /api/cart/addToCart | Username {} not found", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Long userId = user.getId();
        Long itemId = request.getItemId();
        Optional<Item> item = itemRepository.findById(itemId);
        if (!item.isPresent()) {
            log.error("POST /api/cart/addToCart | User {} Item {} not found", userId, itemId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity()).forEach(i ->
                cart.addItem(item.get()));
        cartRepository.save(cart);
        log.info("POST /api/cart/addToCart | User {} Item {}", userId, itemId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<Cart> removeFromCart(@RequestBody ModifyCartRequest request) {
        String username = request.getUsername();
        User user = userRepository.findByUsername(request.getUsername());
        if (user == null) {
            log.error("POST /api/cart/removeFromCart | Username {} not found", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Long userId = user.getId();
        Long itemId = request.getItemId();
        Optional<Item> item = itemRepository.findById(request.getItemId());
        if (!item.isPresent()) {
            log.error("POST /api/cart/removeFromCart | User {} Item {} not found", userId, itemId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Cart cart = user.getCart();
        IntStream.range(0, request.getQuantity()).forEach(i -> cart.removeItem(item.get()));
        cartRepository.save(cart);
        log.info("POST /api/cart/removeFromCart | User {} Item {}", userId, itemId);
        return ResponseEntity.ok(cart);
    }
}
