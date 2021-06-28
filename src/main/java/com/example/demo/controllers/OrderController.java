package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/order")
@RestController
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/submit/{username}")
    public ResponseEntity<UserOrder> submit(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("POST /api/order/submit/{} | Not found", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserOrder order = UserOrder.createFromCart(user.getCart());
        orderRepository.save(order);
        log.info("POST /api/order/submit/{} | Order {}", username, order.getId());
        return ResponseEntity.ok(order);
    }

    @GetMapping("/history/{username}")
    public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("GET /api/history/{} | Not found", username);
            return ResponseEntity.notFound().build();
        }
        log.info("GET /api/history/{}", username);
        return ResponseEntity.ok(orderRepository.findByUser(user));
    }
}
