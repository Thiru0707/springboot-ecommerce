package com.example.ecommerce.controller;

import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    // ✅ Admin: list all products
    @GetMapping("/products")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "admin_products";
    }

    // ✅ Admin: list all orders
    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderRepo.findAll());
        return "admin_orders";
    }

    // ✅ Admin: show edit form
    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productRepo.findById(id).orElseThrow());
        return "edit_product";
    }

    // ✅ Admin: handle edit submission
    @PostMapping("/products/edit")
    public String saveProduct(@ModelAttribute Product product) {
        productRepo.save(product);
        return "redirect:/admin/products";
    }

    // ✅ Admin: delete product
    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/admin/products";
    }
}

