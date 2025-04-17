package com.example.market.repository;

import com.example.market.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    boolean existsByName(String name);


}
