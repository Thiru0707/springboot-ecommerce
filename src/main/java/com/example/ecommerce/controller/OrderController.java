package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/place")
    public Order placeOrder(@RequestParam String paymentId, Authentication auth) {
        User user = userService.getCurrentUser();
        return orderService.placeOrder(user, paymentId);
    }

    @GetMapping
    public List<Order> getOrders(Authentication auth) {
        User user = userService.getCurrentUser();
        return orderService.getOrders(user);
    }

    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }
}

