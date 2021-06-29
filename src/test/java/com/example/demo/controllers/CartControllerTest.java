package com.example.demo.controllers;

import com.example.demo.Helpers;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CartController cartController;

    @Test
    public void testAddToCart() {
        Long id = 1L;
        String username = "alan";
        Long itemId = 1L;
        String name = "box";
        BigDecimal price = BigDecimal.valueOf(1.99);
        int quantity = 2;
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        Item item = Helpers.createItem(itemId, name, price);
        Cart cart = Helpers.createCart(null);
        User user = Helpers.createUser(id, username, cart);
        when(userRepository.findByUsername(username)).thenReturn(user);
        Optional<Item> optionalItem = Optional.ofNullable(item);
        when(itemRepository.findById(itemId)).thenReturn(optionalItem);
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart responseCart = response.getBody();
        assertThat(responseCart).usingRecursiveComparison().isEqualTo(cart);
    }

    @Test
    public void testAddToCartItemNotFound() {
        Long id = 1L;
        String username = "alan";
        Long itemId = 1L;
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        Cart cart = Helpers.createCart(null);
        User user = Helpers.createUser(id, username, cart);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testAddToCartUserNotFound() {
        String username = "alan";
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(null);
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCart() {
        Long id = 1L;
        String username = "alan";
        Long itemId = 1L;
        String name = "box";
        BigDecimal price = BigDecimal.valueOf(1.99);
        int quantity = 1;
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        Item item = Helpers.createItem(itemId, name, price);
        List<Item> items = Arrays.asList(item, item);
        Cart cart = Helpers.createCart(items);
        User user = Helpers.createUser(id, username, cart);
        when(userRepository.findByUsername(username)).thenReturn(user);
        Optional<Item> optionalItem = Optional.ofNullable(item);
        when(itemRepository.findById(itemId)).thenReturn(optionalItem);
        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Cart responseCart = response.getBody();
        assertThat(responseCart).usingRecursiveComparison().isEqualTo(cart);
    }

    @Test
    public void testRemoveFromCartItemNotFound() {
        Long id = 1L;
        String username = "alan";
        Long itemId = 1L;
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        Cart cart = Helpers.createCart(null);
        User user = Helpers.createUser(id, username, cart);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testRemoveFromCartUserNotFound() {
        String username = "alan";
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(null);
        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
