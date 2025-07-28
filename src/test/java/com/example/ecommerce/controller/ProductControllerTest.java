package com.example.ecommerce.controller;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private ProductService productService;

    @Test
    void testGetAllProducts() throws Exception {
        Product p = new Product();
        p.setId(1L);
        p.setName("Laptop");
        p.setPrice(BigDecimal.valueOf(999.99));

        Mockito.when(productService.getAllProducts()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Laptop")));
    }

    @Test
    void testSearchProducts() throws Exception {
        Product p = new Product();
        p.setId(2L);
        p.setName("Phone");

        Mockito.when(productService.searchProducts("phone")).thenReturn(List.of(p));

        mockMvc.perform(get("/api/products/search")
                .param("query", "phone")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Phone")));
    }
}

