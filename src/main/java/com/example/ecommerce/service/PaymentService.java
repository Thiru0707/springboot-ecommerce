package com.example.ecommerce.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {
    public String createPaymentOrder(double amount) {
        // Mock: In real app, call Razorpay API here
        return "ORDER_" + UUID.randomUUID();
    }
}
