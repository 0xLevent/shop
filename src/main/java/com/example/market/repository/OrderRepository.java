package com.example.market.repository;

import com.example.market.entity.Order;
import com.example.market.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findByIdAndUser(Long id, User user);

}