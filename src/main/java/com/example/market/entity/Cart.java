package com.example.market.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Getter
@Setter
public class Cart {

    public void setId(int id) {
        this.id = id;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    public int getId() {
        return id;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public User getUser() {
        return user;
    }

    @OneToOne(mappedBy = "cart")
    private User user;

}
