package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private CartService cartService;
    @MockBean private UserService userService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testAddToCart() throws Exception {
        String payload = """
        {
            "productId": 1,
            "quantity": 2
        }
        """;

        mockMvc.perform(post("/api/cart/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void testGetCart() throws Exception {
        when(cartService.getCartForCurrentUser()).thenReturn(new Cart());

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk());
    }

    @Test
    void testRemoveFromCart() throws Exception {
        mockMvc.perform(delete("/api/cart/remove/5"))
                .andExpect(status().isOk());
    }

    @Test
    void testClearCart() throws Exception {
        mockMvc.perform(delete("/api/cart/clear"))
                .andExpect(status().isOk());
    }
}

