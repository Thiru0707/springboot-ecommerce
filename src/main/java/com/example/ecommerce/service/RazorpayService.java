package com.example.ecommerce.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class RazorpayService {

    @Value("${razorpay.key}")
    private String razorpayKey;

    @Value("${razorpay.secret}")
    private String razorpaySecret;

    public Map<String, String> createOrder(BigDecimal amount) {
        try {
            RazorpayClient client = new RazorpayClient(razorpayKey, razorpaySecret);

            JSONObject options = new JSONObject();
            options.put("amount", amount.multiply(BigDecimal.valueOf(100))); // in paise
            options.put("currency", "INR");
            options.put("payment_capture", 1);

            // ✅ Correct method: lowercase `orders`
            Order order = client.orders.create(options);

            Map<String, String> response = new HashMap<>();
            response.put("id", order.get("id"));
            response.put("amount", order.get("amount").toString());
            response.put("currency", order.get("currency"));
            response.put("status", order.get("status"));
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order", e);
        }
    }
}
