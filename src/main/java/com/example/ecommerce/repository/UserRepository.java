package com.example.ecommerce.repository;

import java.util.Optional;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // ← REQUIRED
    Optional<User> findByEmail(String email);       // ← Already used in cart logic
}
