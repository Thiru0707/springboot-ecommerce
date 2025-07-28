package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProductServiceTest {

    @Mock ProductRepository productRepo;
    @InjectMocks ProductService productService;

    @Test
    void testGetAllProducts() {
        Product p = new Product(); p.setName("Laptop");
        Mockito.when(productRepo.findAll()).thenReturn(Collections.singletonList(p));
        List<Product> products = productService.getAllProducts();
        assertEquals(1, products.size());
    }

    @Test
    void testSearchProducts() {
        Product p = new Product(); p.setName("Phone");
        Mockito.when(productRepo.findByNameContainingIgnoreCase("Phone")).thenReturn(Collections.singletonList(p));
        List<Product> results = productRepo.findByNameContainingIgnoreCase("Phone");
        assertEquals(1, results.size());
    }
}
