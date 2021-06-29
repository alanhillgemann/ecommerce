package com.example.demo;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.math.BigDecimal;
import java.util.List;

public class Helpers {

    public static Cart createCart(List<Item> items) {
        Cart cart = new Cart();
        if (items != null) {
            for (Item item : items) {
                cart.addItem(item);
            }
        }
        return cart;
    }

    public static Item createItem(Long id, String name, BigDecimal price) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPrice(price);
        return item;
    }

    public static User createUser(Long id, String username, Cart cart) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        if (cart != null) {
            user.setCart(cart);
        }
        return user;
    }
}
