package com.example.demo.controllers;

import com.example.demo.Helpers;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemController itemController;

    @Test
    public void testGetItems() {
        Long id = 1L;
        String name = "box";
        BigDecimal price = BigDecimal.valueOf(1.99);
        Item item = Helpers.createItem(id, name, price);
        List<Item> items = Arrays.asList(item);
        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> responseItems = response.getBody();
        Assert.assertEquals(items, responseItems);
    }

    @Test
    public void testGetItemById() {
        Long id = 1L;
        String name = "box";
        BigDecimal price = BigDecimal.valueOf(1.99);
        Item item = Helpers.createItem(id, name, price);
        Optional<Item> optionalItem = Optional.ofNullable(item);
        when(itemRepository.findById(id)).thenReturn(optionalItem);
        ResponseEntity<Item> response = itemController.getItemById(id);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Item responseItem = response.getBody();
        Assert.assertEquals(item, responseItem);
    }

    @Test
    public void testGetItemByIdNotFound() {
        Long id = 1L;
        when(itemRepository.findById(id)).thenReturn(Optional.empty());
        ResponseEntity<Item> response = itemController.getItemById(id);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetItemsByName() {
        Long id = 1L;
        String name = "box";
        BigDecimal price = BigDecimal.valueOf(1.99);
        Item item = Helpers.createItem(id, name, price);
        List<Item> items = Arrays.asList(item);
        when(itemRepository.findByName(name)).thenReturn(items);
        ResponseEntity<List<Item>> response = itemController.getItemsByName(name);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Item> responseItems = response.getBody();
        Assert.assertEquals(items, responseItems);
    }

    @Test
    public void testGetItemsByNameNotFound() {
        String name = "box";
        when(itemRepository.findByName(name)).thenReturn(Collections.emptyList());
        ResponseEntity<List<Item>> response = itemController.getItemsByName(name);
        Assert.assertNotNull(response);
        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
