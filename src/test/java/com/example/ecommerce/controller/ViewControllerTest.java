package com.example.ecommerce.controller;

import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ViewController.class)
public class ViewControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private ProductRepository productRepository;
    @MockBean private CartService cartService;
    @MockBean private UserService userService;
    @MockBean private OrderService orderService;

    @Test
    void testHomePage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void testProductsPage() throws Exception {
        mockMvc.perform(get("/products")).andExpect(status().isOk());
    }

    @Test
    void testCartPage() throws Exception {
        mockMvc.perform(get("/cart")).andExpect(status().isOk());
    }
}
