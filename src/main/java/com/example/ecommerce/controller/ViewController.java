package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.UserService;
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
    private final UserService userService;

    @Value("${razorpay.key}")
    private String razorpayKey;

    // ========================== Home Page ==========================
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "index";
    }

    // ========================== Product List (with optional search) ==========================
    @GetMapping("/products")
    public String listProducts(@RequestParam(value = "search", required = false) String search, Model model) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("products", productRepository.findByNameContainingIgnoreCase(search));
        } else {
            model.addAttribute("products", productRepository.findAll());
        }
        return "products";
    }

    // ========================== Cart ==========================
    @GetMapping("/cart")
    public String viewCart(Model model) {
        Cart cart = cartService.getCartForCurrentUser();
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/cart/update/{productId}")
    public String updateCartItem(@PathVariable Long productId, @RequestParam int quantity) {
        cartService.updateQuantity(productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{productId}")
    public String removeFromCart(@PathVariable Long productId) {
        cartService.removeFromCart(productId);
        return "redirect:/cart";
    }

    // ========================== Checkout ==========================
    @GetMapping("/checkout")
    public String checkout(Model model) {
        Cart cart = cartService.getCartForCurrentUser();
        model.addAttribute("amount", cart.getTotal().intValue());
        model.addAttribute("razorpayKey", razorpayKey);
        return "checkout";
    }

    // ========================== Profile Page ==========================
    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "profile";
    }
}

