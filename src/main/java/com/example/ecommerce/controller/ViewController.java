package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final ProductRepository productRepository;
    private final CartService cartService;

    @Value("${razorpay.key}")
    private String razorpayKey;

    // Home page
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "index";
    }

    // Product list (with optional search)
    @GetMapping("/products")
    public String listProducts(@RequestParam(value = "search", required = false) String search, Model model) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("products", productRepository.findByNameContainingIgnoreCase(search));
        } else {
            model.addAttribute("products", productRepository.findAll());
        }
        return "products";
    }

    // View cart
    @GetMapping("/cart")
    public String viewCart(Model model) {
        Cart cart = cartService.getCartForCurrentUser();
        model.addAttribute("cart", cart);
        return "cart";
    }

    // Checkout page
    @GetMapping("/checkout")
    public String checkout(Model model) {
        Cart cart = cartService.getCartForCurrentUser();
        model.addAttribute("amount", cart.getTotal().intValue());
        model.addAttribute("razorpayKey", razorpayKey);
        return "checkout";
    }
}


