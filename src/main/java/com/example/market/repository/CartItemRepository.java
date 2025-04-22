package com.example.market.repository;

import com.example.market.entity.Cart;
import com.example.market.entity.CartItem;
import com.example.market.entity.Item;
import com.example.market.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByCartAndItem(Cart cart, Item item);


}
