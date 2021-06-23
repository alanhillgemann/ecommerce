package com.example.demo.model.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user_order")
public class UserOrder {

	@Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
    @JsonProperty
    private Long id;

	@Column
    @JsonProperty
	@ManyToMany(cascade = CascadeType.ALL)
    private List<Item> items;

    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    @JsonProperty
	@ManyToOne
	private User user;

	@Column
    @JsonProperty
    private BigDecimal total;

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

	public static UserOrder createFromCart(Cart cart) {
		UserOrder order = new UserOrder();
		order.setItems(cart.getItems().stream().collect(Collectors.toList()));
		order.setTotal(cart.getTotal());
		order.setUser(cart.getUser());
		return order;
	}
}
