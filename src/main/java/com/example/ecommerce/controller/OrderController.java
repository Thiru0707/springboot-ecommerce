package com.example.ecommerce.controller;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    // ✅ User: view own orders page
    @GetMapping("/orders")
    public String myOrders(Model model) {
        User user = userService.getCurrentUser();
        List<Order> orders = orderService.getOrders(user);
        model.addAttribute("orders", orders);
        return "orders"; // templates/orders.html
    }

    // ✅ User: view single order details page
    @GetMapping("/orders/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order_details"; // templates/order_details.html
    }

    // ✅ Optional: REST API - place order (for AJAX)
    @PostMapping("/api/orders/place")
    @ResponseBody
    public Order placeOrder(@RequestParam String paymentId) {
        User user = userService.getCurrentUser();
        return orderService.placeOrder(user, paymentId);
    }

    // ✅ Optional: REST API - get user's orders as JSON
    @GetMapping("/api/orders")
    @ResponseBody
    public List<Order> getOrders() {
        User user = userService.getCurrentUser();
        return orderService.getOrders(user);
    }
}

