package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final ProductRepository productRepository;
    private final CartService cartService;
    private final UserService userService;
    private final OrderService orderService;

    @Value("${razorpay.key}")
    private String razorpayKey;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "index";
    }

    @GetMapping("/products")
    public String listProducts(@RequestParam(value = "search", required = false) String search, Model model) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("products", productRepository.findByNameContainingIgnoreCase(search));
        } else {
            model.addAttribute("products", productRepository.findAll());
        }
        return "products";
    }

    @GetMapping("/cart")
    public String viewCart(Model model) {
        Cart cart = cartService.getCartForCurrentUser();
        model.addAttribute("cart", cart);
        return "cart";
    }

    @GetMapping("/orders")
    public String myOrders(Model model) {
        List<Order> orders = orderService.getOrdersForCurrentUser();
        model.addAttribute("orders", orders);
        return "orders";
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

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Cart cart = cartService.getCartForCurrentUser();
        model.addAttribute("amount", cart.getTotal().intValue());
        model.addAttribute("razorpayKey", razorpayKey);
        return "checkout";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        return "profile";
    }
}


