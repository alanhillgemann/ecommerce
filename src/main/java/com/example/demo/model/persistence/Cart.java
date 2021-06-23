package com.example.demo.model.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonProperty
    private Long id;

    @Column
    @JsonProperty
    @ManyToMany
    private List<Item> items;

    @Column
    @JsonProperty
    private BigDecimal total;

    @JsonProperty
    @OneToOne(mappedBy = "cart")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addItem(Item item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(item);
        if (total == null) {
            total = new BigDecimal(0);
        }
        total = total.add(item.getPrice());
    }

    public void removeItem(Item item) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.remove(item);
        if (total == null) {
            total = new BigDecimal(0);
        }
        total = total.subtract(item.getPrice());
    }
}
