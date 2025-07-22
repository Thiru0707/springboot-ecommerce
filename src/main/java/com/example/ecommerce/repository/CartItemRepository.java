package com.example.ecommerce.repository;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart_User(User user); // ✅ Corrected path

    Optional<CartItem> findByCart_UserAndProduct_Id(User user, Long productId); // ✅ Corrected path
}
