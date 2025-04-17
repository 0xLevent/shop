package com.example.market.repository;

import com.example.market.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository <Cart, Integer> {
}
