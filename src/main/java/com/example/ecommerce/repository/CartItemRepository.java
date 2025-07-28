package com.example.ecommerce.repository;

import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart_User(User user);
    Optional<CartItem> findByCart_UserAndProduct_Id(User user, Long productId);
}
