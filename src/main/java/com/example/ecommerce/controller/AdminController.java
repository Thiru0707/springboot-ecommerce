package com.example.ecommerce.controller;

import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    // Show all products (for web)
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "admin_products";
    }

    // Show all orders (for web)
    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderRepo.findAll());
        return "admin_orders";
    }

    // Show edit form (for web)
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productRepo.findById(id).orElseThrow());
        return "edit_product";
    }

    // Save edited product (for web)
    @PostMapping("/products/edit")
    public String saveProduct(@ModelAttribute Product product) {
        productRepo.save(product);
        return "redirect:/admin/products";
    }

    // Delete product (for web)
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/admin/products";
    }

    // ✅ NEW: API endpoint to create a product via JSON (for Postman/API)
    @PostMapping("/products")
    @ResponseBody
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productRepo.save(product);
        return ResponseEntity.ok(savedProduct);
    }
}
