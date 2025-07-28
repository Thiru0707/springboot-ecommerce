package com.example.ecommerce.controller;

import com.example.ecommerce.service.RazorpayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private RazorpayService razorpayService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCreateOrder() throws Exception {
        // Arrange: Mock RazorpayService
        Map<String, String> mockResponse = Map.of(
                "id", "order_Hg123456",
                "amount", "50000",
                "currency", "INR",
                "status", "created"
        );

        Mockito.when(razorpayService.createOrder(any(BigDecimal.class))).thenReturn(mockResponse);

        // JSON body
        String payload = """
        {
            "amount": 500
        }
        """;

        // Act + Assert
        mockMvc.perform(post("/api/payment/create-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("order_Hg123456"))
                .andExpect(jsonPath("$.amount").value("50000"))
                .andExpect(jsonPath("$.currency").value("INR"))
                .andExpect(jsonPath("$.status").value("created"));
    }
}
