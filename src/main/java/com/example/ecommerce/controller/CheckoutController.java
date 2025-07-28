package com.example.ecommerce.controller;

import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @Value("${razorpay.key}")
    private String razorpayKey;

    // Show checkout page
    @GetMapping("/checkout")
    public String checkout(Model model) {
        var cart = cartService.getCartForCurrentUser();
        model.addAttribute("amount", cart.getTotal().intValue());
        model.addAttribute("razorpayKey", razorpayKey);
        return "checkout";
    }

    // Handle payment success (simplified)
    @PostMapping("/checkout/success")
    public String paymentSuccess(@RequestParam String paymentId) {
        var user = userService.getCurrentUser();
        Order order = orderService.placeOrder(user, paymentId);
        return "redirect:/orders/" + order.getId();
    }
}
