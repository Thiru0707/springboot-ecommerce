package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.CartItem;
import com.example.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public Cart getCart() {
        return cartService.getCartForCurrentUser();
    }

    @PostMapping("/add")
    public CartItem addToCart(@RequestParam Long productId, @RequestParam int quantity) {
        return cartService.addToCart(productId, quantity);
    }

    @PutMapping("/update")
    public void updateQuantity(@RequestParam Long productId, @RequestParam int quantity) {
        cartService.updateQuantity(productId, quantity);
    }

    @DeleteMapping("/remove")
    public void removeFromCart(@RequestParam Long productId) {
        cartService.removeFromCart(productId);
    }

    @DeleteMapping("/clear")
    public void clearCart() {
        cartService.clearCart(cartService.getCurrentUserEmail());
    }
}

