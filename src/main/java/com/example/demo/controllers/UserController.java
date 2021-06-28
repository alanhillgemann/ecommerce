package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api/user")
@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/id/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            log.error("GET /api/user/id/{} | Not found", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("GET /api/user/id/{}", id);
        return ResponseEntity.of(user);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> findByUserName(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("GET /api/user/{} | Not found", username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("GET /api/user/{}", username);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
        String username = createUserRequest.getUsername();
        User user = userRepository.findByUsername(username);
        if (user != null) {
            log.error("GET /api/user/create | Username {} exists", username);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User newUser = new User();
        newUser.setUsername(createUserRequest.getUsername());
        Cart cart = new Cart();
        cartRepository.save(cart);
        newUser.setCart(cart);
        String password = createUserRequest.getPassword();
        int passwordLength = password.length();
        String confirmPassword = createUserRequest.getConfirmPassword();
        if (passwordLength < 7 || !password.equals(confirmPassword)) {
            log.error("POST /api/user/create | Password not valid", username, passwordLength);
            return ResponseEntity.badRequest().build();
        }
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(newUser);
        log.info("POST /api/user/create | User {}", newUser.getId());
        return ResponseEntity.ok(newUser);
    }
}
