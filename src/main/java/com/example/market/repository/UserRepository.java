package com.example.market.repository;

import com.example.market.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "cart")
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<User> findByIdWithCart(@Param("userId") Long userId);
}
