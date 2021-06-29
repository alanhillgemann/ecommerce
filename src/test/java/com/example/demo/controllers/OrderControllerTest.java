package com.example.demo.controllers;

import com.example.demo.Helpers;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderController orderController;

    @Test
    public void testGetOrdersForUser() {
        Long id = 1L;
        String username = "alan";
        Long itemId = 2L;
        String name = "box";
        BigDecimal price = BigDecimal.valueOf(1.99);
        Item item = Helpers.createItem(itemId, name, price);
        List<Item> items = Arrays.asList(item);
        Cart cart = Helpers.createCart(items);
        User user = Helpers.createUser(id, username, cart);
        UserOrder userOrder = UserOrder.createFromCart(user.getCart());
        List<UserOrder> userOrders = Arrays.asList(userOrder);
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrders);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserOrder> responseUserOrders = response.getBody();
        Assert.assertEquals(userOrders, responseUserOrders);
    }

    @Test
    public void testGetOrdersForUserNotFound() {
        String username = "alan";
        when(userRepository.findByUsername(username)).thenReturn(null);
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(username);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSubmit() {
        Long id = 1L;
        String username = "alan";
        Long itemId = 2L;
        String name = "box";
        BigDecimal price = BigDecimal.valueOf(1.99);
        Item item = Helpers.createItem(itemId, name, price);
        List<Item> items = Arrays.asList(item);
        Cart cart = Helpers.createCart(items);
        User user = Helpers.createUser(id, username, cart);
        UserOrder userOrder = UserOrder.createFromCart(user.getCart());
        when(userRepository.findByUsername(username)).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit(username);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        UserOrder responseUserOrder = response.getBody();
        assertThat(responseUserOrder).usingRecursiveComparison().isEqualTo(userOrder);
    }

    @Test
    public void testSubmitUserNotFound() {
        String username = "alan";
        when(userRepository.findByUsername(username)).thenReturn(null);
        ResponseEntity<UserOrder> response = orderController.submit(username);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
