package com.example.market.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    @Email
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;


    public String getEmail() {
        return email;
    }

    public Cart getCart() {
        return cart;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public Integer getId() {
        return id;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}